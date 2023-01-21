package com.minerarcana.naming.network;

import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.minerarcana.naming.target.INamingTarget;
import com.minerarcana.naming.target.NamingTargets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class NameTargetMessage {
    private final INamingTarget namingTarget;
    private final String name;

    public NameTargetMessage(INamingTarget namingTarget, @Nullable String name) {
        this.namingTarget = namingTarget;
        this.name = name;
    }

    public void encode(FriendlyByteBuf packetBuffer) {
        NamingTargets.INSTANCE.toPacketBuffer(namingTarget, packetBuffer);
        packetBuffer.writeBoolean(name != null);
        if (name != null) {
            packetBuffer.writeUtf(name);
        }
    }

    public void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        if (namingTarget != null) {
            contextSupplier.get().enqueueWork(() -> {
                ServerPlayer player = contextSupplier.get().getSender();
                if (player != null) {
                    boolean hasAbility = player.getCapability(Namer.CAP)
                            .map(cap -> cap.hasAbility("naming"))
                            .orElse(false);
                    if (hasAbility) {
                        ServerLevel level = player.getLevel();
                        namingTarget.hydrate(level);
                        if (namingTarget.isValid(player)) {
                            namingTarget.name(name, player);
                            NamingCriteriaTriggers.NAMING.trigger(player, namingTarget.getTarget());
                        }
                    }

                }
            });
        }
    }

    public static NameTargetMessage decode(FriendlyByteBuf packetBuffer) {
        return new NameTargetMessage(
                NamingTargets.INSTANCE.fromPacketBuffer(packetBuffer),
                packetBuffer.readBoolean() ? packetBuffer.readUtf() : null
        );
    }
}
