package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.container.SpeakingContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

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

    public void setTargetName(int index, String targetName) {
        this.targetNames[index] = targetName;
        this.setChanged();
    }

    public String getTargetName(int index) {
        return this.targetNames[index];
    }

    @Override
    protected void loadMessages(CompoundNBT nbt) {
        super.loadMessages(nbt);
        ListNBT speakingTargetsListNBT = nbt.getList("speakingTargets", Constants.NBT.TAG_STRING);
        for (int i = 0; i < speakingTargetsListNBT.size(); i++) {
            this.speakingTargets[i] = SpeakingTarget.valueOf(speakingTargetsListNBT.getString(i));
        }
        ListNBT targetNamesNBT = nbt.getList("targetNames", Constants.NBT.TAG_STRING);
        for (int i = 0; i < targetNamesNBT.size(); i++) {
            this.targetNames[i] = targetNamesNBT.getString(i);
        }
    }

    @Nonnull
    protected CompoundNBT saveMessages(@Nonnull CompoundNBT nbt) {
        nbt = super.saveMessages(nbt);
        ListNBT speakingTargetsNBT = new ListNBT();
        for (SpeakingTarget speakingTarget : speakingTargets) {
            speakingTargetsNBT.add(StringNBT.valueOf(speakingTarget.name()));
        }
        nbt.put("speakingTargets", speakingTargetsNBT);
        ListNBT targetNamesNBT = new ListNBT();
        for (String speakingTargetName : targetNames) {
            targetNamesNBT.add(StringNBT.valueOf(speakingTargetName));
        }
        nbt.put("targetNames", targetNamesNBT);
        return nbt;
    }
}
