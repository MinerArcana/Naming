package com.minerarcana.naming.spell;

import net.minecraft.entity.Entity;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public enum SpellTargeting implements ISpellTargeting {
    AABB_32 {
        @Override
        public List<Entity> getTargeted(Entity caster, Predicate<Entity> matcher) {
            return caster.level.getEntities(
                    (Entity) null,
                    caster.getBoundingBox().inflate(32),
                    matcher
            );
        }

        @Override
        public boolean needsClient() {
            return true;
        }
    },
    NONE {
        @Override
        public List<Entity> getTargeted(Entity caster, Predicate<Entity> matcher) {
            return Collections.emptyList();
        }

        @Override
        public boolean needsClient() {
            return true;
        }
    }
}
