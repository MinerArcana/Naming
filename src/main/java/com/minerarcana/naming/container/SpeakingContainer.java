package com.minerarcana.naming.container;

import com.google.common.collect.Lists;
import com.minerarcana.naming.blockentity.SpeakingStoneBlockEntity;
import com.minerarcana.naming.blockentity.SpeakingTarget;
import com.minerarcana.naming.content.NamingBlocks;
import com.minerarcana.naming.content.NamingContainers;
import com.minerarcana.naming.network.property.Property;
import com.minerarcana.naming.network.property.PropertyTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SpeakingContainer extends MessageContainer<SpeakingTarget> {
    private final List<Property<String>> targetNameProperties;

    public SpeakingContainer(int containerId, SpeakingStoneBlockEntity blockEntity) {
        super(
                NamingContainers.SPEAKING.get(),
                containerId,
                blockEntity,
                blockEntity::getSpeakingTarget,
                blockEntity::setSpeakingTarget
        );
        this.targetNameProperties = Lists.newArrayList();
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            targetNameProperties.add(
                    this.getPropertyManager().addTrackedProperty(PropertyTypes.STRING.create(
                            () -> blockEntity.getTargetName(finalI),
                            (type) -> blockEntity.setTargetName(finalI, type)
                    ))
            );
        }
    }

    public SpeakingContainer(@Nullable ContainerType<?> type, int containerId, PlayerInventory playerInventory) {
        super(type, containerId, playerInventory);
        this.targetNameProperties = Lists.newArrayList();
        for (int i = 0; i < 4; i++) {
            targetNameProperties.add(
                    this.getPropertyManager().addTrackedProperty(PropertyTypes.STRING)
            );
        }
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity pPlayer) {
        return Container.stillValid(this.getCallable(), pPlayer, NamingBlocks.SPEAKING_STONE.get());
    }

    public List<Property<String>> getTargetNameProperties() {
        return targetNameProperties;
    }

    @Override
    public Class<SpeakingTarget> getEnum() {
        return SpeakingTarget.class;
    }
}
