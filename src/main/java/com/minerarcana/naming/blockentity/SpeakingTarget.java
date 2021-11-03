package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.advancement.criteria.messaged.MessageTarget;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.minerarcana.naming.content.NamingText;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

public enum SpeakingTarget implements IButtoned<SpeakingTarget> {
    NONE(NamingText.NONE, false) {
        @Override
        public boolean speak(ITextComponent spoken, SpeakingStoneBlockEntity blockEntity) {
            return false;
        }
    },
    OWNER(NamingText.OWNER, false) {
        @Override
        public boolean speak(ITextComponent spoken, SpeakingStoneBlockEntity blockEntity) {
            PlayerEntity owner = blockEntity.getOwner();
            if (owner instanceof ServerPlayerEntity) {
                owner.sendMessage(spoken, owner.getUUID());
                owner.getCapability(Namer.CAP)
                        .ifPresent(namer -> namer.speakTo(spoken.getContents()));
                NamingCriteriaTriggers.MESSAGED.trigger((ServerPlayerEntity) owner, spoken.getContents(), MessageTarget.SPEAK_TO);
                return true;
            }
            return false;
        }
    },
    NEARBY(NamingText.NEARBY, false) {
        @Override
        public boolean speak(ITextComponent spoken, SpeakingStoneBlockEntity blockEntity) {
            return false;
        }
    },
    LISTENERS(NamingText.LISTENERS, true) {
        @Override
        public boolean speak(ITextComponent spoken, SpeakingStoneBlockEntity blockEntity) {
            if (blockEntity.getLevel() instanceof ServerWorld) {
                return ((ServerWorld) blockEntity.getLevel())
                        .getDataStorage()
                        .computeIfAbsent(ListeningWorldData::new, ListeningWorldData.NAME)
                        .speakTo(spoken.getContents())
                        .isListening();
            }
            return false;
        }
    };

    private final ITextComponent title;
    private final boolean needsTargetName;

    SpeakingTarget(ITextComponent title, boolean needsTargetName) {
        this.title = title;
        this.needsTargetName = needsTargetName;
    }

    @Override
    public ITextComponent getMessage() {
        return title;
    }

    public boolean isNeedsTargetName() {
        return needsTargetName;
    }

    @Override
    public SpeakingTarget cycle() {
        switch (this) {
            case NONE:
                return OWNER;
            case OWNER:
                return NEARBY;
            case NEARBY:
                return LISTENERS;
            default:
                return NONE;
        }
    }

    public abstract boolean speak(ITextComponent spoken, SpeakingStoneBlockEntity blockEntity);
}
