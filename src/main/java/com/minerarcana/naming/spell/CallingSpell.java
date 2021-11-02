package com.minerarcana.naming.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.EntityPosWrapper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class CallingSpell extends Spell {
    @Override
    public boolean cast(@Nonnull Entity caster, String spoken) {
        List<Entity> entityList = caster.level.getEntities(
                (Entity) null,
                caster.getBoundingBox().inflate(32),
                entity -> entity.getName()
                        .getContents()
                        .toLowerCase(Locale.ROOT)
                        .contains(spoken.toLowerCase(Locale.ROOT))
        );

        entityList.forEach(entity -> {
            if (entity instanceof LivingEntity) {
                Brain<?> brain = ((LivingEntity) entity).getBrain();
                if (brain.checkMemory(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED)) {
                    brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(caster, false));
                    brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityPosWrapper(caster, false), 0.5F, 2));
                }
                if (entity instanceof MobEntity && caster instanceof LivingEntity) {
                    if (!(entity instanceof TameableEntity) || !((TameableEntity) entity).isTame() ||
                            ((TameableEntity) entity).getOwner() != caster)
                        ((MobEntity) entity).setTarget((LivingEntity) caster);
                }
            }
        });

        return !entityList.isEmpty();
    }
}
