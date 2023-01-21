package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.advancement.criteria.messaged.MessageTarget;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.minerarcana.naming.content.NamingText;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

import java.util.List;

public enum SpeakingTarget implements IButtoned<SpeakingTarget> {
    NONE(NamingText.NONE, false) {
        @Override
        public boolean speak(Component spoken, SpeakingStoneBlockEntity blockEntity) {
            return false;
        }
    },
    OWNER(NamingText.OWNER, false) {
        @Override
        public boolean speak(Component spoken, SpeakingStoneBlockEntity blockEntity) {
            Player owner = blockEntity.getOwner();
            if (owner instanceof ServerPlayer serverPlayer) {
                owner.sendSystemMessage(spoken);
                owner.getCapability(Namer.CAP)
                        .ifPresent(namer -> namer.speakTo(spoken.getString()));
                NamingCriteriaTriggers.MESSAGED.trigger(serverPlayer, spoken.getString(), MessageTarget.SPEAK_TO);
                return true;
            }
            return false;
        }
    },
    NEARBY(NamingText.NEARBY, false) {
        @Override
        public boolean speak(Component spoken, SpeakingStoneBlockEntity blockEntity) {
            AABB nearby = new AABB(blockEntity.getBlockPos()).inflate(32);
            if (blockEntity.getLevel() instanceof ServerLevel serverLevel) {
                List<Player> nearbyPlayers = serverLevel.getEntities(EntityTypeTest.forClass(Player.class), nearby, entity -> true);
                for (Player player: nearbyPlayers) {
                    player.sendSystemMessage(spoken);
                }
                return !nearbyPlayers.isEmpty();
            }
            return false;
        }
    },
    LISTENERS(NamingText.LISTENERS, true) {
        @Override
        public boolean speak(Component spoken, SpeakingStoneBlockEntity blockEntity) {
            if (blockEntity.getLevel() instanceof ServerLevel) {
                return ((ServerLevel) blockEntity.getLevel())
                        .getDataStorage()
                        .computeIfAbsent(ListeningWorldData::new, ListeningWorldData::new, ListeningWorldData.NAME)
                        .speakTo(spoken.getString())
                        .isListening();
            }
            return false;
        }
    };

    private final Component title;
    private final boolean needsTargetName;

    SpeakingTarget(Component title, boolean needsTargetName) {
        this.title = title;
        this.needsTargetName = needsTargetName;
    }

    @Override
    public Component getMessage() {
        return title;
    }

    public boolean isNeedsTargetName() {
        return needsTargetName;
    }

    @Override
    public SpeakingTarget cycle() {
        return switch (this) {
            case NONE -> OWNER;
            case OWNER -> NEARBY;
            case NEARBY -> LISTENERS;
            default -> NONE;
        };
    }

    public abstract boolean speak(Component spoken, SpeakingStoneBlockEntity blockEntity);
}
