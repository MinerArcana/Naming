package com.minerarcana.naming.advancement.criteria.naming;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.minerarcana.naming.Naming;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class NamingCriterionTrigger extends AbstractCriterionTrigger<NamingCriterionInstance> {
    public static final ResourceLocation ID = Naming.rl("naming");

    public void trigger(ServerPlayerEntity player, Object named) {
        this.trigger(player, instance -> instance.matches(player.getLevel(), named));
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected NamingCriterionInstance createInstance(JsonObject pJson, EntityPredicate.AndPredicate pEntityPredicate,
                                                     ConditionArrayParser pConditionsParser) {
        String type = JSONUtils.getAsString(pJson, "type");
        switch (type) {
            case "entity":
                return entityNaming(pJson, pEntityPredicate);
            case "item":
                return itemNaming(pJson, pEntityPredicate);
            default:
                throw new JsonParseException("Invalid Type: " + type);
        }
    }

    private static NamingCriterionInstance entityNaming(JsonObject jsonObject, EntityPredicate.AndPredicate playerPredicate) {
        if (jsonObject.has("predicate")) {
            return new EntityNamingCriterionInstance(playerPredicate, EntityPredicate.fromJson(jsonObject.get("predicate")));
        } else if (jsonObject.has("checker")) {
            return new EntityNamingCriterionInstance(
                    playerPredicate,
                    EntityChecker.getByName(JSONUtils.getAsString(jsonObject, "checker"))
                            .mapRight(JsonParseException::new)
                            .orThrow()
            );
        } else {
            throw new JsonParseException("Missing 'predicate' or 'check'");
        }
    }

    private static NamingCriterionInstance itemNaming(JsonObject jsonObject, EntityPredicate.AndPredicate playerPredicate) {
        return new ItemStackNamingCriterionInstance(playerPredicate, Ingredient.fromJson(jsonObject.get("item")));
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    public NamingCriterionInstance item(Ingredient ingredient) {
        return new ItemStackNamingCriterionInstance(EntityPredicate.AndPredicate.ANY, ingredient);
    }

    public NamingCriterionInstance entityPredicate(EntityPredicate entityPredicate) {
        return new EntityNamingCriterionInstance(EntityPredicate.AndPredicate.ANY, entityPredicate);
    }

    public NamingCriterionInstance checker(EntityChecker checker) {
        return new EntityNamingCriterionInstance(EntityPredicate.AndPredicate.ANY, checker);
    }
}
