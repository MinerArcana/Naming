package com.minerarcana.naming.advancement.criteria.naming;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

import Ingredient;

public class ItemStackNamingCriterionInstance extends NamingCriterionInstance {
    private final Ingredient ingredient;

    public ItemStackNamingCriterionInstance(EntityPredicate.AndPredicate predicate, Ingredient ingredient) {
        super(predicate);
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(ServerWorld world, Object object) {
        if (object instanceof ItemStack) {
            return ingredient.test((ItemStack) object);
        }
        return false;
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull ConditionArraySerializer pConditions) {
        JsonObject jsonObject = super.serializeToJson(pConditions);
        jsonObject.add("item", ingredient.toJson());
        return jsonObject;
    }

    @Override
    public String getType() {
        return "item";
    }
}
