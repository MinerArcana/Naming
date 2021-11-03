package com.minerarcana.naming.spell;

import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.util.math.EntityPosWrapper;

import javax.annotation.Nonnull;
import java.util.Collection;

public class CallingSpell extends Spell {
    @Override
    public boolean cast(@Nonnull Entity caster, INamer namer, String spoken, Collection<Entity> targeted) {
        targeted.forEach(entity -> {
            if (entity instanceof LivingEntity) {
                Brain<?> brain = ((LivingEntity) entity).getBrain();
                if (brain.checkMemory(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED)) {
                    brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(caster, false));
                    brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityPosWrapper(caster, false), 0.5F, 2));
                }
                if (entity instanceof MobEntity && caster instanceof LivingEntity && !entity.isAlliedTo(caster)) {
                    ((MobEntity) entity).setTarget((LivingEntity) caster);
                }
            }
        });

        return !targeted.isEmpty();
    }
}
