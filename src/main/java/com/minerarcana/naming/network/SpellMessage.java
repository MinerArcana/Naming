package com.minerarcana.naming.network;

import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingEffects;
import com.minerarcana.naming.spell.Spell;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SpellMessage {
    private final Spell spell;
    private final String input;

    public SpellMessage(Spell spell, String input) {
        this.spell = spell;
        this.input = input;
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeRegistryId(spell);
        packetBuffer.writeUtf(input);
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayerEntity entity = contextSupplier.get().getSender();
            if (entity != null) {
                if (!entity.hasEffect(NamingEffects.HOARSE.get())) {
                    entity.getCapability(Namer.CAP)
                            .ifPresent(namer -> namer.castSpell(entity, spell, input));
                }
            }
        });
        return true;
    }

    public static SpellMessage decode(PacketBuffer packetBuffer) {
        return new SpellMessage(
                packetBuffer.readRegistryId(),
                packetBuffer.readUtf()
        );
    }
}
