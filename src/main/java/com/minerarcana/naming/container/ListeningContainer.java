package com.minerarcana.naming.container;

import com.minerarcana.naming.blockentity.ListeningStoneBlockEntity;
import com.minerarcana.naming.blockentity.ListeningType;
import com.minerarcana.naming.content.NamingBlocks;
import com.minerarcana.naming.content.NamingContainers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

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

    public ListeningContainer(@Nullable MenuType<?> type, int containerId, Inventory playerInventory) {
        super(type, containerId, playerInventory);
    }

    @Override
    public boolean stillValid(@Nonnull Player pPlayer) {
        return AbstractContainerMenu.stillValid(this.getCallable(), pPlayer, NamingBlocks.LISTENING_STONE.get());
    }

    @Override
    public Class<ListeningType> getEnum() {
        return ListeningType.class;
    }
}
