package com.minerarcana.naming.network;

import com.google.common.collect.Sets;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.util.ClientGetter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

public class SyncNamingMessage {
    private final Collection<String> abilities;

    public SyncNamingMessage(Collection<String> abilities) {
        this.abilities = abilities;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(abilities.size());
        for (String ability : abilities) {
            buffer.writeUtf(ability);
        }
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            PlayerEntity player = ClientGetter.getPlayerEntity();
            if (player != null) {
                player.getCapability(Namer.CAP)
                        .ifPresent(cap -> {
                            if (cap instanceof Namer) {
                                ((Namer) cap).setAbilities(abilities);
                            }
                        });
            }
        });
        return true;
    }

    public static SyncNamingMessage decode(PacketBuffer packetBuffer) {
        int length = packetBuffer.readInt();
        Set<String> abilities = Sets.newHashSet();
        for (int x = 0; x < length; x++) {
            abilities.add(packetBuffer.readUtf());
        }
        return new SyncNamingMessage(abilities);
    }
}
