package com.minerarcana.naming.network;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.spell.Spell;
import com.minerarcana.naming.util.ClientGetter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpellClientMessage {
    private final Spell spell;
    private final String input;

    public SpellClientMessage(Spell spell, String input) {
        this.spell = spell;
        this.input = input;
    }

    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeRegistryId(spell);
        packetBuffer.writeUtf(input);
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Player player = ClientGetter.getPlayerEntity();
            if (player != null) {
                Naming.network.spellToServer(
                        spell,
                        input,
                        spell.getTargeting()
                                .getTargeted(
                                        player,
                                        entity -> {
                                            if (entity.getCustomName() != null) {
                                                return entity.getCustomName()
                                                        .getContents()
                                                        .equalsIgnoreCase(input);
                                            }
                                            return false;
                                        }
                                )
                                .stream()
                                .mapToInt(Entity::getId)
                                .toArray()
                );
            }
        });
        return true;
    }

    public static SpellClientMessage decode(FriendlyByteBuf packetBuffer) {
        return new SpellClientMessage(
                packetBuffer.readRegistryId(),
                packetBuffer.readUtf()
        );
    }
}
