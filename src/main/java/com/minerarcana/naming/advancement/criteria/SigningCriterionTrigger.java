package com.minerarcana.naming.advancement.criteria;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.minerarcana.naming.Naming;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SigningCriterionTrigger extends AbstractCriterionTrigger<SigningCriterionInstance> {
    private static final ResourceLocation ID = Naming.rl("signing");

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected SigningCriterionInstance createInstance(JsonObject jsonObject, EntityPredicate.AndPredicate entityPredicate,
                                                      ConditionArrayParser conditionsParser) {
        JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("title");
        String title = jsonPrimitive == null ? null : jsonPrimitive.getAsString();
        Ingredient item = jsonObject.has("item") ? Ingredient.fromJson(jsonObject.get("item")) : null;
        return new SigningCriterionInstance(ID, title, item, entityPredicate);
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity playerEntity, ItemStack itemStack, String bookTitle) {
        this.trigger(playerEntity, instance -> instance.matches(itemStack, bookTitle));
    }

    public SigningCriterionInstance create(IItemProvider item) {
        return create(null, Ingredient.of(item));
    }

    public SigningCriterionInstance create(String title, Ingredient item) {
        return new SigningCriterionInstance(ID, title, item, EntityPredicate.AndPredicate.ANY);
    }
}
