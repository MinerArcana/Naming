package com.minerarcana.naming.container;

import com.minerarcana.naming.blockentity.SpeakingStoneBlockEntity;
import com.minerarcana.naming.blockentity.SpeakingTarget;
import com.minerarcana.naming.content.NamingContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nullable;

public class SpeakingContainer extends MessageContainer<SpeakingTarget> {
    public SpeakingContainer(int containerId, SpeakingStoneBlockEntity blockEntity) {
        super(
                NamingContainers.SPEAKING.get(),
                containerId,
                blockEntity,
                blockEntity::getSpeakingTarget,
                blockEntity::setSpeakingTarget
        );
    }

    public SpeakingContainer(@Nullable ContainerType<?> type, int containerId, PlayerInventory playerInventory) {
        super(type, containerId, playerInventory);
    }

    @Override
    public Class<SpeakingTarget> getEnum() {
        return SpeakingTarget.class;
    }
}
