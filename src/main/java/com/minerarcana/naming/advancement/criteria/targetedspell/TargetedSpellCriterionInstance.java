package com.minerarcana.naming.advancement.criteria.targetedspell;

import com.google.gson.JsonObject;
import com.minerarcana.naming.advancement.criteria.EntityChecker;
import com.minerarcana.naming.spell.Spell;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityPredicate.AndPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArraySerializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;

public class TargetedSpellCriterionInstance extends CriterionInstance {
    private final Spell spell;
    private final EntityChecker entityChecker;
    private final EntityPredicate entityPredicate;

    public TargetedSpellCriterionInstance(AndPredicate playerPredicate, @Nullable Spell spell, EntityChecker entityChecker) {
        super(TargetedSpellCriterionTrigger.ID, playerPredicate);
        this.spell = spell;
        this.entityChecker = entityChecker;
        this.entityPredicate = null;
    }

    public TargetedSpellCriterionInstance(AndPredicate playerPredicate, @Nullable Spell spell, EntityPredicate entityPredicate) {
        super(TargetedSpellCriterionTrigger.ID, playerPredicate);
        this.spell = spell;
        this.entityChecker = null;
        this.entityPredicate = entityPredicate;
    }

    public boolean matches(ServerPlayerEntity player, Spell spell, Entity target) {
        if (this.spell == null || spell == this.spell) {
            if (entityChecker != null) {
                return entityChecker.matches(target);
            } else if (entityPredicate != null) {
                return entityPredicate.matches(player, target);
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull ConditionArraySerializer pConditions) {
        JsonObject jsonObject = super.serializeToJson(pConditions);
        if (spell != null) {
            jsonObject.addProperty("spell", Objects.requireNonNull(spell.getRegistryName()).toString());
        }
        if (entityPredicate != null) {
            jsonObject.add("predicate", entityPredicate.serializeToJson());
        } else if (entityChecker != null) {
            jsonObject.addProperty("checker", entityChecker.name().toLowerCase(Locale.ROOT));
        }
        return jsonObject;
    }
}
