package com.minerarcana.naming.network;

import com.google.common.collect.Sets;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.util.ClientGetter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

public class SyncNamingMessage {
    private final Collection<String> abilities;

    public SyncNamingMessage(Collection<String> abilities) {
        this.abilities = abilities;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(abilities.size());
        for (String ability : abilities) {
            buffer.writeUtf(ability);
        }
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Player player = ClientGetter.getPlayerEntity();
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

    public static SyncNamingMessage decode(FriendlyByteBuf packetBuffer) {
        int length = packetBuffer.readInt();
        Set<String> abilities = Sets.newHashSet();
        for (int x = 0; x < length; x++) {
            abilities.add(packetBuffer.readUtf());
        }
        return new SyncNamingMessage(abilities);
    }
}
