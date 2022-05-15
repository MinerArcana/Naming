package com.minerarcana.naming.advancement.criteria.signing;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.minerarcana.naming.Naming;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SigningCriterionTrigger extends SimpleCriterionTrigger<SigningCriterionInstance> {
    private static final ResourceLocation ID = Naming.rl("signing");

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected SigningCriterionInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite entityPredicate,
                                                      DeserializationContext conditionsParser) {
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

    public void trigger(ServerPlayer playerEntity, ItemStack itemStack, String bookTitle) {
        this.trigger(playerEntity, instance -> instance.matches(itemStack, bookTitle));
    }

    public SigningCriterionInstance create(ItemLike item) {
        return create(null, Ingredient.of(item));
    }

    public SigningCriterionInstance create(String title, Ingredient item) {
        return new SigningCriterionInstance(ID, title, item, EntityPredicate.Composite.ANY);
    }
}
