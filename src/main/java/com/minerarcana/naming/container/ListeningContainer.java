package com.minerarcana.naming.container;

import com.minerarcana.naming.blockentity.ListeningStoneBlockEntity;
import com.minerarcana.naming.blockentity.ListeningType;
import com.minerarcana.naming.content.NamingContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nullable;

public class ListeningContainer extends MessageContainer<ListeningType> {
    public ListeningContainer(int containerId, ListeningStoneBlockEntity blockEntity) {
        super(
                NamingContainers.LISTENING.get(),
                containerId,
                blockEntity,
                blockEntity::getListeningType,
                blockEntity::setListeningType
        );
    }

    public ListeningContainer(@Nullable ContainerType<?> type, int containerId, PlayerInventory playerInventory) {
        super(type, containerId, playerInventory);
    }

    @Override
    public Class<ListeningType> getEnum() {
        return ListeningType.class;
    }
}
