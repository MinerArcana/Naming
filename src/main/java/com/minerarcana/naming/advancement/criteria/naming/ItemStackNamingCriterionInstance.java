package com.minerarcana.naming.advancement.criteria.naming;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;


public class ItemStackNamingCriterionInstance extends NamingCriterionInstance {
    private final Ingredient ingredient;

    public ItemStackNamingCriterionInstance(EntityPredicate.Composite predicate, Ingredient ingredient) {
        super(predicate);
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(ServerLevel world, Object object) {
        if (object instanceof ItemStack) {
            return ingredient.test((ItemStack) object);
        }
        return false;
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull SerializationContext pConditions) {
        JsonObject jsonObject = super.serializeToJson(pConditions);
        jsonObject.add("item", ingredient.toJson());
        return jsonObject;
    }

    @Override
    public String getType() {
        return "item";
    }
}
