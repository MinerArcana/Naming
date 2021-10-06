package com.minerarcana.naming.advancement.criteria;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.minerarcana.naming.Naming;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;

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
        return new SigningCriterionInstance(ID, title, entityPredicate);
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity playerEntity, String bookTitle) {
        this.trigger(playerEntity, instance -> instance.matches(bookTitle));
    }

    public SigningCriterionInstance create() {
        return create(null);
    }

    public SigningCriterionInstance create(String title) {
        return new SigningCriterionInstance(ID, title, EntityPredicate.AndPredicate.ANY);
    }
}
