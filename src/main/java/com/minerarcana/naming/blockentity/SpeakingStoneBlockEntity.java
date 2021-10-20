package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.container.MessageContainer;
import com.minerarcana.naming.container.SpeakingContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class SpeakingStoneBlockEntity extends MessageBlockEntity {
    private final SpeakingTarget[] speakingTargets = {
            SpeakingTarget.NONE,
            SpeakingTarget.NONE,
            SpeakingTarget.NONE,
            SpeakingTarget.NONE
    };

    private WeakReference<PlayerEntity> ownerReference;
    private UUID ownerUUID;

    private boolean heard = false;

    public SpeakingStoneBlockEntity(TileEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Override
    public boolean renderSide(Direction side) {
        return side.getAxis() != Direction.Axis.Y;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new SpeakingContainer(
                containerId,
                this
        );
    }

    public void setOwner(@Nullable PlayerEntity entity) {
        this.ownerReference = new WeakReference<>(entity);
        this.ownerUUID = entity != null ? entity.getUUID() : null;
    }

    @Nullable
    public PlayerEntity getOwner() {
        if (this.ownerReference == null || this.ownerReference.get() == null) {
            if (this.getLevel() != null && this.ownerUUID != null) {
                this.ownerReference = new WeakReference<>(this.getLevel()
                        .getPlayerByUUID(ownerUUID)
                );
                return this.ownerReference.get();
            }
        } else {
            return this.ownerReference.get();
        }

        return null;
    }

    public void setSpeakingTarget(int index, SpeakingTarget speakingTarget) {
        this.speakingTargets[index] = speakingTarget;
        this.setChanged();
    }

    public SpeakingTarget getSpeakingTarget(int index) {
        return this.speakingTargets[index];
    }

    public void setHeard(boolean heard) {
        if (this.heard != heard) {
            this.heard = heard;
            this.setChanged();
        }
    }

    public boolean getHeard() {
        return heard;
    }
}
