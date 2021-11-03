package com.minerarcana.naming.spell;

import net.minecraft.entity.Entity;

import java.util.List;
import java.util.function.Predicate;

public interface ISpellTargeting {
    List<Entity> getTargeted(Entity caster, Predicate<Entity> matcher);
}
