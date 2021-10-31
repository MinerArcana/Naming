package com.minerarcana.naming.container;

import com.minerarcana.naming.blockentity.ListeningStoneBlockEntity;
import com.minerarcana.naming.blockentity.ListeningType;
import com.minerarcana.naming.content.NamingBlocks;
import com.minerarcana.naming.content.NamingContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nonnull;
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
    public boolean stillValid(@Nonnull PlayerEntity pPlayer) {
        return Container.stillValid(this.getCallable(), pPlayer, NamingBlocks.LISTENING_STONE.get());
    }

    @Override
    public Class<ListeningType> getEnum() {
        return ListeningType.class;
    }
}
