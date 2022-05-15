package com.minerarcana.naming.worlddata;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.spell.Spell;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class EchoingData {
    private final Spell spell;
    private final String input;
    private final int timesToEcho;
    private final long echoTime;
    private final WeakEntityReference entityReference;

    public EchoingData(Spell spell, String input, int timesToEcho, long echoTime, WeakEntityReference entityReference) {
        this.spell = spell;
        this.input = input;
        this.timesToEcho = timesToEcho;
        this.echoTime = echoTime;
        this.entityReference = entityReference;
    }

    public long getEchoTime() {
        return echoTime;
    }

    public EchoingData doEcho(LevelAccessor world) {
        Entity entity = this.entityReference.get(world);
        if (entity != null && world instanceof ServerLevel) {
            if (spell == null) {
                ((ServerLevel) world).getServer()
                        .getPlayerList()
                        .broadcastMessage(
                                new TextComponent(input),
                                ChatType.CHAT,
                                entity.getUUID()
                        );
            } else {
                if (entity instanceof ServerPlayer) {
                    Naming.network.spellToClient((ServerPlayer) entity, spell, input);
                } else {
                    entity.getCapability(Namer.CAP)
                            .ifPresent(namer -> namer.castSpell(
                                    entity,
                                    spell,
                                    input,
                                    () -> spell.getTargeting().getTargeted(entity, targeted -> {
                                        if (targeted.getCustomName() != null) {
                                            return targeted.getCustomName()
                                                    .getContents()
                                                    .equalsIgnoreCase(input);
                                        } else {
                                            return targeted.getType().getRegistryName() != null &&
                                                    targeted.getType().getRegistryName().toString().equalsIgnoreCase(input);
                                        }
                                    }))
                            );
                }
            }
        }

        return timesToEcho == 0 ? null : new EchoingData(
                spell,
                input,
                timesToEcho - 1,
                world.getLevelData().getGameTime() + 120,
                entityReference
        );
    }
}
