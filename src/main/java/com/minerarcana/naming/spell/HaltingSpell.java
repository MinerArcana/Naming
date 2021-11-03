package com.minerarcana.naming.spell;

import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;
import java.util.Collection;

public class HaltingSpell extends Spell {
    @Override
    public boolean cast(@Nonnull Entity caster, INamer namer, String spoken, Collection<Entity> targeted) {
        targeted.forEach(entity -> {
            entity.setDeltaMovement(Vector3d.ZERO);
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 4));
                ((LivingEntity) entity).addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 100, 4));
            }
        });

        return !targeted.isEmpty();
    }
}
