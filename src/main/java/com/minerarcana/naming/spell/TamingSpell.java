package com.minerarcana.naming.spell;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.LanguageMap;

import javax.annotation.Nonnull;
import java.util.Collection;

public class TamingSpell extends Spell {
    private String altDescriptionId;

    @Override
    public boolean cast(@Nonnull Entity caster, INamer namer, String spoken, Collection<Entity> targeted) {
        targeted.forEach(entity -> {
            if (entity instanceof IAngerable) {
                ((IAngerable) entity).stopBeingAngry();
            }
            if (entity instanceof TameableEntity && caster instanceof PlayerEntity) {
                TameableEntity tameableEntity = (TameableEntity) entity;
                if (!tameableEntity.isTame()) {
                    tameableEntity.tame((PlayerEntity) caster);
                }
            }
        });
        return !targeted.isEmpty();
    }

    @Override
    public boolean matches(String spell, String spoken) {
        return spell.equalsIgnoreCase(LanguageMap.getInstance().getOrDefault(this.getDescriptionId())) ||
                spell.equalsIgnoreCase(LanguageMap.getInstance().getOrDefault(this.getAltDescriptionId()));
    }

    public String getAltDescriptionId() {
        if (altDescriptionId == null) {
            this.altDescriptionId = Util.makeDescriptionId("spell", Naming.rl("taming_alt"));
        }
        return this.altDescriptionId;
    }
}
