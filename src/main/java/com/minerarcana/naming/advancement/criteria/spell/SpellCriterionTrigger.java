package com.minerarcana.naming.advancement.criteria.spell;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingRegistries;
import com.minerarcana.naming.spell.Spell;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SpellCriterionTrigger extends SimpleCriterionTrigger<SpellCriterionInstance> {
    public static final ResourceLocation ID = Naming.rl("spell");

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected SpellCriterionInstance createInstance(JsonObject pJson, EntityPredicate.Composite pEntityPredicate, DeserializationContext pConditionsParser) {
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
        return new SpellCriterionInstance(pEntityPredicate, spell, GsonHelper.getAsInt(pJson, "castings", 1));
    }

    public void trigger(ServerPlayer playerEntity) {
        playerEntity.getCapability(Namer.CAP)
                .ifPresent(namer -> this.trigger(
                        playerEntity,
                        spellCriterionInstance -> spellCriterionInstance.matches(namer)
                ));
    }

    public SpellCriterionInstance forTotal(int total) {
        return new SpellCriterionInstance(EntityPredicate.Composite.ANY, null, total);
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }
}
