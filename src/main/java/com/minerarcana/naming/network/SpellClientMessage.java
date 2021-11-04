package com.minerarcana.naming.network;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.spell.Spell;
import com.minerarcana.naming.util.ClientGetter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SpellClientMessage {
    private final Spell spell;
    private final String input;


    public SpellClientMessage(Spell spell, String input) {
        this.spell = spell;
        this.input = input;
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeRegistryId(spell);
        packetBuffer.writeUtf(input);
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            PlayerEntity player = ClientGetter.getPlayerEntity();
            if (player != null) {
                Naming.network.spellToServer(
                        spell,
                        input,
                        spell.getTargeting()
                                .getTargeted(
                                        Minecraft.getInstance().player,
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

    public static SpellClientMessage decode(PacketBuffer packetBuffer) {
        return new SpellClientMessage(
                packetBuffer.readRegistryId(),
                packetBuffer.readUtf()
        );
    }
}
