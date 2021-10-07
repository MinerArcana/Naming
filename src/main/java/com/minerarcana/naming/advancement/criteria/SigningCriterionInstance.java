package com.minerarcana.naming.advancement.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class SigningCriterionInstance extends CriterionInstance {
    private final String title;
    private final Ingredient item;

    public SigningCriterionInstance(ResourceLocation id, String title, Ingredient item, EntityPredicate.AndPredicate predicate) {
        super(id, predicate);
        this.title = title;
        this.item = item;
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull ConditionArraySerializer conditions) {
        JsonObject jsonObject = super.serializeToJson(conditions);
        if (title != null) {
            jsonObject.addProperty("title", title);
        }
        if (item != null) {
            jsonObject.add("item", item.toJson());
        }
        return jsonObject;
    }

    public boolean matches(ItemStack itemStack, String bookTitle) {
        return (title == null || title.equalsIgnoreCase(bookTitle)) &&
                (item == null || item.test(itemStack));
    }
}
