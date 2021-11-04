package com.minerarcana.naming.network;

import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingEffects;
import com.minerarcana.naming.spell.Spell;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SpellServerMessage {
    private final Spell spell;
    private final String input;
    private final int[] targeted;

    public SpellServerMessage(Spell spell, String input, int[] targeted) {
        this.spell = spell;
        this.input = input;
        this.targeted = targeted;
    }

    public void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeRegistryId(spell);
        packetBuffer.writeUtf(input);
        packetBuffer.writeVarIntArray(targeted);
    }

    public boolean consume(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayerEntity caster = contextSupplier.get().getSender();
            if (caster != null) {
                if (!caster.hasEffect(NamingEffects.HOARSE.get())) {
                    caster.getCapability(Namer.CAP)
                            .ifPresent(namer -> namer.castSpell(caster, spell, input, () -> {
                                IntList integers = IntArrayList.wrap(targeted);
                                return spell.getTargeting()
                                        .getTargeted(caster, target -> integers.contains(target.getId()));
                            }));
                }
            }
        });
        return true;
    }

    public static SpellServerMessage decode(PacketBuffer packetBuffer) {
        return new SpellServerMessage(
                packetBuffer.readRegistryId(),
                packetBuffer.readUtf(),
                packetBuffer.readVarIntArray()
        );
    }
}
