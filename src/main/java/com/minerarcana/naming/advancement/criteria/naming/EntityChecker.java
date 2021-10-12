package com.minerarcana.naming.advancement.criteria.naming;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum EntityChecker {
    HOSTILE {
        @Override
        public boolean matches(Entity entity) {
            return entity instanceof IMob;
        }
    },
    TAMED {
        @Override
        public boolean matches(Entity entity) {
            return entity instanceof TameableEntity && ((TameableEntity) entity).isTame();
        }
    };

    public static Either<EntityChecker, String> getByName(String checkerName) {
        for (EntityChecker checker : values()) {
            if (checker.name().equalsIgnoreCase(checkerName)) {
                return Either.left(checker);
            }
        }

        return Either.right(checkerName + " does not exist in " + Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(","))
        );
    }

    public abstract boolean matches(Entity entity);
}
