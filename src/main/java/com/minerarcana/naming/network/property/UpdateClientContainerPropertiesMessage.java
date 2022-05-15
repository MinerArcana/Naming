package com.minerarcana.naming.network.property;

import com.google.common.collect.Lists;
import com.minerarcana.naming.util.ClientGetter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.function.Supplier;

public class UpdateClientContainerPropertiesMessage {
    private final short windowId;
    private final List<Triple<PropertyType<?>, Short, Object>> updates;

    public UpdateClientContainerPropertiesMessage(short windowId, List<Triple<PropertyType<?>, Short, Object>> updates) {
        this.windowId = windowId;
        this.updates = updates;
    }

    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeShort(windowId);
        List<Triple<PropertyType<?>, Short, Object>> validUpdates = Lists.newArrayList();
        for (Triple<PropertyType<?>, Short, Object> update : updates) {
            if (update.getLeft().isValid(update.getRight())) {
                validUpdates.add(update);
            }
        }

        packetBuffer.writeShort(validUpdates.size());
        for (Triple<PropertyType<?>, Short, Object> update : validUpdates) {
            packetBuffer.writeShort(PropertyTypes.getIndex(update.getLeft()));
            packetBuffer.writeShort(update.getMiddle());
            update.getLeft().attemptWrite(packetBuffer, update.getRight());
        }
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Player playerEntity = ClientGetter.getPlayerEntity();
            if (playerEntity != null) {
                AbstractContainerMenu container = playerEntity.containerMenu;
                if (container.containerId == windowId) {
                    if (container instanceof IPropertyManaged) {
                        PropertyManager propertyManager = ((IPropertyManaged) container).getPropertyManager();
                        for (Triple<PropertyType<?>, Short, Object> update : updates) {
                            propertyManager.update(update.getLeft(), update.getMiddle(), update.getRight());
                        }
                    }
                }
            }
        });
        return true;
    }

    public static UpdateClientContainerPropertiesMessage decode(FriendlyByteBuf packetBuffer) {
        short windowId = packetBuffer.readShort();
        short updateAmount = packetBuffer.readShort();
        List<Triple<PropertyType<?>, Short, Object>> updates = Lists.newArrayList();
        for (short i = 0; i < updateAmount; i++) {
            PropertyType<?> propertyType = PropertyTypes.getByIndex(packetBuffer.readShort());
            short propertyLocation = packetBuffer.readShort();
            Object object = propertyType.getReader().apply(packetBuffer);
            updates.add(Triple.of(propertyType, propertyLocation, object));
        }
        return new UpdateClientContainerPropertiesMessage(windowId, updates);
    }
}
