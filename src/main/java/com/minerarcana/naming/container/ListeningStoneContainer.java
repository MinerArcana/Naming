package com.minerarcana.naming.container;

import com.google.common.collect.Lists;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.blockentity.ListeningStoneBlockEntity;
import com.minerarcana.naming.blockentity.ListeningType;
import com.minerarcana.naming.content.NamingBlocks;
import com.minerarcana.naming.content.NamingContainers;
import com.minerarcana.naming.mixin.ContainerAccessor;
import com.minerarcana.naming.network.property.IPropertyManaged;
import com.minerarcana.naming.network.property.Property;
import com.minerarcana.naming.network.property.PropertyManager;
import com.minerarcana.naming.network.property.PropertyTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListeningStoneContainer extends Container implements IPropertyManaged {
    private final IWorldPosCallable callable;
    private final PropertyManager propertyManager;

    private final Property<String> name;
    private final List<Pair<Property<Integer>, Property<String>>> listeners;

    public ListeningStoneContainer(int containerId, ListeningStoneBlockEntity blockEntity) {
        super(NamingContainers.LISTENING_STONE.get(), containerId);
        this.callable = IWorldPosCallable.create(
                Objects.requireNonNull(blockEntity.getLevel()),
                blockEntity.getBlockPos()
        );
        this.propertyManager = Naming.properties.createManager(containerId);
        this.name = this.propertyManager.addTrackedProperty(PropertyTypes.STRING.create(
                blockEntity::getName,
                blockEntity::setName
        ));
        this.listeners = Lists.newArrayList();
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            listeners.add(Pair.of(
                    this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(
                            () -> blockEntity.getListeningType(finalI).ordinal(),
                            listenType -> blockEntity.setListeningType(finalI, ListeningType.values()[finalI]))
                    ),
                    this.propertyManager.addTrackedProperty(PropertyTypes.STRING.create(
                            () -> blockEntity.getMessage(finalI).getContents(),
                            message -> blockEntity.setMessage(finalI, new StringTextComponent(message)))
                    )
            ));
        }
    }

    @SuppressWarnings("unused")
    public ListeningStoneContainer(@Nullable ContainerType<?> type, int containerId, PlayerInventory playerInventory) {
        super(type, containerId);
        this.callable = IWorldPosCallable.NULL;
        this.propertyManager = Naming.properties.createManager(containerId);
        this.name = this.propertyManager.addTrackedProperty(PropertyTypes.STRING);
        this.listeners = Lists.newArrayList();
        for (int i = 0; i < 4; i++) {
            listeners.add(Pair.of(
                    this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER),
                    this.propertyManager.addTrackedProperty(PropertyTypes.STRING)
            ));
        }
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity pPlayer) {
        return Container.stillValid(callable, pPlayer, NamingBlocks.LISTENING_STONE.get());
    }

    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.propertyManager.updateClient(((ContainerAccessor) this).getContainerListeners(), false);
    }

    @Override
    public void addSlotListener(@Nonnull IContainerListener pListener) {
        super.addSlotListener(pListener);
        this.propertyManager.updateClient(Collections.singleton(pListener), true);
    }

    public List<Pair<Property<Integer>, Property<String>>> getListeners() {
        return listeners;
    }

    public Property<String> getName() {
        return name;
    }
}
