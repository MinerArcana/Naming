package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.container.SpeakingContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
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

    private final String[] targetNames = {
            "",
            "",
            "",
            ""
    };

    private WeakReference<Player> ownerReference;
    private UUID ownerUUID;

    private boolean heard = false;

    public SpeakingStoneBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    @Override
    public boolean renderSide(Direction side) {
        return side.getAxis() != Direction.Axis.Y;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player playerEntity) {
        return new SpeakingContainer(
                containerId,
                this
        );
    }

    public void setOwner(@Nullable Player entity) {
        this.ownerReference = new WeakReference<>(entity);
        this.ownerUUID = entity != null ? entity.getUUID() : null;
    }

    @Nullable
    public Player getOwner() {
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

    public void setTargetName(int index, String targetName) {
        this.targetNames[index] = targetName;
        this.setChanged();
    }

    public String getTargetName(int index) {
        return this.targetNames[index];
    }

    @Override
    protected void loadMessages(CompoundTag nbt) {
        super.loadMessages(nbt);
        ListTag speakingTargetsListNBT = nbt.getList("speakingTargets", Tag.TAG_STRING);
        for (int i = 0; i < speakingTargetsListNBT.size(); i++) {
            this.speakingTargets[i] = SpeakingTarget.valueOf(speakingTargetsListNBT.getString(i));
        }
        ListTag targetNamesNBT = nbt.getList("targetNames", Tag.TAG_STRING);
        for (int i = 0; i < targetNamesNBT.size(); i++) {
            this.targetNames[i] = targetNamesNBT.getString(i);
        }
    }

    @Nonnull
    protected CompoundTag saveMessages(@Nonnull CompoundTag nbt) {
        nbt = super.saveMessages(nbt);
        ListTag speakingTargetsNBT = new ListTag();
        for (SpeakingTarget speakingTarget : speakingTargets) {
            speakingTargetsNBT.add(StringTag.valueOf(speakingTarget.name()));
        }
        nbt.put("speakingTargets", speakingTargetsNBT);
        ListTag targetNamesNBT = new ListTag();
        for (String speakingTargetName : targetNames) {
            targetNamesNBT.add(StringTag.valueOf(speakingTargetName));
        }
        nbt.put("targetNames", targetNamesNBT);
        return nbt;
    }
}
