package com.minerarcana.naming.advancement.criteria.spell;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingRegistries;
import com.minerarcana.naming.spell.Spell;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.EntityPredicate.AndPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SpellCriterionTrigger extends AbstractCriterionTrigger<SpellCriterionInstance> {
    public static final ResourceLocation ID = Naming.rl("spell");

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected SpellCriterionInstance createInstance(JsonObject pJson, AndPredicate pEntityPredicate, ConditionArrayParser pConditionsParser) {
        Spell spell = null;
        if (pJson.has("spell")) {
            ResourceLocation spellName = ResourceLocation.tryParse(pJson.getAsJsonPrimitive("spell").getAsString());
            if (spellName != null) {
                spell = NamingRegistries.SPELL.get().getValue(spellName);
                if (spell == null) {
                    throw new JsonParseException("Failed to find spell: " + spellName);
                }
            } else {
                throw new JsonParseException("spell name was not a valid Resource Location");
            }
        }
        return new SpellCriterionInstance(pEntityPredicate, spell, JSONUtils.getAsInt(pJson, "castings", 1));
    }

    public void trigger(ServerPlayerEntity playerEntity) {
        playerEntity.getCapability(Namer.CAP)
                .ifPresent(namer -> this.trigger(
                        playerEntity,
                        spellCriterionInstance -> spellCriterionInstance.matches(namer)
                ));
    }

    public SpellCriterionInstance forTotal(int total) {
        return new SpellCriterionInstance(AndPredicate.ANY, null, total);
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }
}
