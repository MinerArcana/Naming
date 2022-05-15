package com.minerarcana.naming.advancement.criteria.signing;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class SigningCriterionInstance extends AbstractCriterionTriggerInstance {
    private final String title;
    private final Ingredient item;

    public SigningCriterionInstance(ResourceLocation id, String title, Ingredient item, EntityPredicate.Composite predicate) {
        super(id, predicate);
        this.title = title;
        this.item = item;
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull SerializationContext conditions) {
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
