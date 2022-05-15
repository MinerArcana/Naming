package com.minerarcana.naming.advancement.criteria.spell;

import com.google.gson.JsonObject;
import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.spell.Spell;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class SpellCriterionInstance extends AbstractCriterionTriggerInstance {
    private final Spell spell;
    private final int castings;

    public SpellCriterionInstance(EntityPredicate.Composite playerPredicate, @Nullable Spell spell, int castings) {
        super(SpellCriterionTrigger.ID, playerPredicate);
        this.spell = spell;
        this.castings = castings;
    }

    public boolean matches(INamer namer) {
        if (spell == null) {
            return namer.getTotalCastings() >= castings;
        } else {
            return namer.getTimesCast(spell) >= castings;
        }
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull SerializationContext pConditions) {
        JsonObject jsonObject = super.serializeToJson(pConditions);
        jsonObject.addProperty("castings", castings);
        if (spell != null) {
            jsonObject.addProperty("spell", Objects.requireNonNull(spell.getRegistryName()).toString());
        }
        return jsonObject;
    }
}
