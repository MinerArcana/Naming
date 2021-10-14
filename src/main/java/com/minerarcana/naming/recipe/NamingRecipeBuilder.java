package com.minerarcana.naming.recipe;

import com.google.gson.JsonObject;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.content.NamingRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class NamingRecipeBuilder {
    private final ItemStack result;
    private Ingredient ingredient;
    private Pattern pattern;

    public NamingRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public NamingRecipeBuilder withIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public NamingRecipeBuilder withPattern(Pattern pattern) {
        this.pattern = pattern;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> recipeConsumer, String path) {
        this.build(recipeConsumer, Naming.rl(path));
    }

    public void build(Consumer<IFinishedRecipe> recipeConsumer, ResourceLocation id) {
        recipeConsumer.accept(new NamingFinishedRecipe(
                id,
                result,
                ingredient,
                pattern
        ));
    }

    public static NamingRecipeBuilder of(IItemProvider item) {
        return new NamingRecipeBuilder(new ItemStack(item));
    }

    public static class NamingFinishedRecipe implements IFinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack result;
        private final Ingredient ingredient;
        private final Pattern pattern;

        public NamingFinishedRecipe(ResourceLocation id, ItemStack result, Ingredient ingredient, Pattern pattern) {
            this.id = id;
            this.result = result;
            this.ingredient = ingredient;
            this.pattern = pattern;
        }

        @Override
        public void serializeRecipeData(@Nonnull JsonObject pJson) {
            pJson.add("ingredient", ingredient.toJson());
            pJson.addProperty("pattern", pattern.pattern());
            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("id", Objects.requireNonNull(result.getItem().getRegistryName()).toString());
            resultObject.addProperty("count", result.getCount());
            if (result.getTag() != null) {
                resultObject.addProperty("tag", result.getTag().toString());
            }
            pJson.add("result", resultObject);
        }

        @Override
        @Nonnull
        public ResourceLocation getId() {
            return id;
        }

        @Override
        @Nonnull
        public IRecipeSerializer<?> getType() {
            return NamingRecipes.NAMING_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
