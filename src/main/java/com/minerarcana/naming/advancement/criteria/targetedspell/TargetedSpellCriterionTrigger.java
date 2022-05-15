package com.minerarcana.naming.advancement.criteria.targetedspell;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.advancement.criteria.EntityChecker;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingRegistries;
import com.minerarcana.naming.spell.Spell;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class TargetedSpellCriterionTrigger extends SimpleCriterionTrigger<TargetedSpellCriterionInstance> {
    public static final ResourceLocation ID = Naming.rl("targeted_spell");

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected TargetedSpellCriterionInstance createInstance(JsonObject pJson, EntityPredicate.Composite pEntityPredicate, DeserializationContext pConditionsParser) {
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
        if (pJson.has("predicate")) {
            return new TargetedSpellCriterionInstance(pEntityPredicate, spell, EntityPredicate.fromJson(pJson.get("predicate")));
        } else if (pJson.has("checker")) {
            return new TargetedSpellCriterionInstance(
                    pEntityPredicate,
                    spell,
                    EntityChecker.getByName(GsonHelper.getAsString(pJson, "checker"))
                            .mapRight(JsonParseException::new)
                            .orThrow()
            );
        } else {
            throw new JsonParseException("Missing 'predicate' or 'check'");
        }
    }

    public void trigger(ServerPlayer playerEntity, Spell spell, Entity targeted) {
        playerEntity.getCapability(Namer.CAP)
                .ifPresent(namer -> this.trigger(
                        playerEntity,
                        spellCriterionInstance -> spellCriterionInstance.matches(playerEntity, spell, targeted)
                ));

    }

    public TargetedSpellCriterionInstance forEntity(@Nullable Spell spell, EntityPredicate entityPredicate) {
        return new TargetedSpellCriterionInstance(EntityPredicate.Composite.ANY, spell, entityPredicate);
    }

    public TargetedSpellCriterionInstance forChecker(@Nonnull Spell spell, EntityChecker checker) {
        return new TargetedSpellCriterionInstance(EntityPredicate.Composite.ANY, spell, checker);
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }
}
