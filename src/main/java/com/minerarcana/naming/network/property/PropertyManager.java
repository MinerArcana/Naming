package com.minerarcana.naming.network.property;

import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ContainerListener;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collection;
import java.util.List;

public class PropertyManager {
    private final List<Property<?>> properties;
    private final short containerId;
    private final PropertyInstance propertyInstance;

    public PropertyManager(short containerId, PropertyInstance propertyInstance) {
        this.containerId = containerId;
        this.properties = Lists.newArrayList();
        this.propertyInstance = propertyInstance;
    }

    public <T> Property<T> addTrackedProperty(PropertyType<T> propertyType) {
        Property<T> property = propertyType.create();
        this.properties.add(property);
        return property;
    }

    public <T> Property<T> addTrackedProperty(Property<T> property) {
        this.properties.add(property);
        return property;
    }

    public <T> void updateServer(Property<T> property, T value) {
        short propertyId = -1;
        for (short i = 0; i < properties.size(); i++) {
            if (properties.get(i) == property) {
                propertyId = i;
            }
        }
        property.set(value);
        propertyInstance.getNetwork()
                .sendServerUpdate(new UpdateServerContainerPropertyMessage(
                        containerId,
                        property.getPropertyType(),
                        propertyId,
                        value
                ));
    }

    public void updateClient(ServerPlayer serverPlayer, boolean firstTime) {

        List<Triple<PropertyType<?>, Short, Object>> dirtyProperties = Lists.newArrayList();
        for (short i = 0; i < properties.size(); i++) {
            Property<?> property = properties.get(i);
            if (property.isDirty() || firstTime) {
                dirtyProperties.add(Triple.of(property.getPropertyType(), i, property.get()));
            }
        }

        if (!dirtyProperties.isEmpty()) {
            propertyInstance.getNetwork()
                    .sendClientUpdate(
                            serverPlayer,
                            new UpdateClientContainerPropertiesMessage(
                                    containerId,
                                    dirtyProperties
                            )
                    );
        }
    }

    public void update(PropertyType<?> propertyType, short propertyId, Object value) {
        if (propertyId < properties.size()) {
            Property<?> property = properties.get(propertyId);
            if (property != null && property.getPropertyType() == propertyType) {
                try {
                    propertyType.attemptSet(value, property);
                } catch (ClassCastException e) {
                    propertyInstance.getLogger().warn("Failed to set Property", e);
                }
            }
        }
    }
}
