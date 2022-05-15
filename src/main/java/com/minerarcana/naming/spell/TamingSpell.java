package com.minerarcana.naming.spell;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.Util;
import net.minecraft.locale.Language;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.Collection;

public class TamingSpell extends Spell {
    private String altDescriptionId;

    @Override
    public boolean cast(@Nonnull Entity caster, INamer namer, String spoken, Collection<Entity> targeted) {
        targeted.forEach(entity -> {
            if (entity instanceof NeutralMob) {
                ((NeutralMob) entity).stopBeingAngry();
            }
            if (entity instanceof TamableAnimal tamableEntity && caster instanceof Player) {
                if (!tamableEntity.isTame()) {
                    tamableEntity.tame((Player) caster);
                }
            }
        });
        return !targeted.isEmpty();
    }

    @Override
    public boolean matches(String spell, String spoken) {
        return spell.equalsIgnoreCase(Language.getInstance().getOrDefault(this.getDescriptionId())) ||
                spell.equalsIgnoreCase(Language.getInstance().getOrDefault(this.getAltDescriptionId()));
    }

    public String getAltDescriptionId() {
        if (altDescriptionId == null) {
            this.altDescriptionId = Util.makeDescriptionId("spell", Naming.rl("taming_alt"));
        }
        return this.altDescriptionId;
    }
}
