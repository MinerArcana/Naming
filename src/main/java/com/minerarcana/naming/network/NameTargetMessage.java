package com.minerarcana.naming.network;

import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.minerarcana.naming.target.INamingTarget;
import com.minerarcana.naming.target.NamingTargets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NameTargetMessage {
    private final INamingTarget namingTarget;
    private final String name;

    public NameTargetMessage(INamingTarget namingTarget, String name) {
        this.namingTarget = namingTarget;
        this.name = name;
    }

    public void encode(PacketBuffer packetBuffer) {
        NamingTargets.INSTANCE.toPacketBuffer(namingTarget, packetBuffer);
        packetBuffer.writeUtf(name);
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        if (namingTarget != null) {
            contextSupplier.get().enqueueWork(() -> {
                ServerPlayerEntity player = contextSupplier.get().getSender();
                if (player != null) {
                    ServerWorld level = player.getLevel();
                    namingTarget.hydrate(level);
                    if (namingTarget.isValid(player)) {
                        namingTarget.name(name);
                        NamingCriteriaTriggers.NAMING.trigger(player, namingTarget.getTarget());
                    }
                }
            });
        }

        return true;
    }

    public static NameTargetMessage decode(PacketBuffer packetBuffer) {
        return new NameTargetMessage(NamingTargets.INSTANCE.fromPacketBuffer(packetBuffer), packetBuffer.readUtf(3276));
    }
}
