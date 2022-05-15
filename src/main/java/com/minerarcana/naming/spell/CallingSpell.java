package com.minerarcana.naming.spell;

import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import javax.annotation.Nonnull;
import java.util.Collection;

public class CallingSpell extends Spell {
    @Override
    public boolean cast(@Nonnull Entity caster, INamer namer, String spoken, Collection<Entity> targeted) {
        targeted.forEach(entity -> {
            if (entity instanceof LivingEntity) {
                Brain<?> brain = ((LivingEntity) entity).getBrain();
                if (brain.checkMemory(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED)) {
                    brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(caster, false));
                    brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(caster, 0.5F, 2));
                }
                if (entity instanceof Mob && caster instanceof LivingEntity && !entity.isAlliedTo(caster)) {
                    ((Mob) entity).setTarget((LivingEntity) caster);
                }
            }
        });

        return !targeted.isEmpty();
    }
}
