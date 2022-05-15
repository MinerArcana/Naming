package com.minerarcana.naming.recipe;

import com.google.gson.JsonObject;
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

import Ingredient;
import ItemStack;

public class NamingRecipeBuilder {
    private final ItemStack result;
    private Ingredient ingredient;
    private Pattern pattern;
    private String ability;

    public NamingRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public NamingRecipeBuilder withIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public NamingRecipeBuilder withAbility(String ability) {
        this.ability = ability;
        return this;
    }

    public NamingRecipeBuilder withPattern(String pattern) {
        return this.withPattern(Pattern.compile(pattern));
    }

    public NamingRecipeBuilder withPattern(Pattern pattern) {
        this.pattern = pattern;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> recipeConsumer) {
        this.build(recipeConsumer, result.getItem().getRegistryName());
    }

    public void build(Consumer<IFinishedRecipe> recipeConsumer, ResourceLocation id) {
        Objects.requireNonNull(result, "Result cannot be null");
        Objects.requireNonNull(ingredient, "Ingredient cannot be null");
        Objects.requireNonNull(pattern, "Pattern cannot be null");

        recipeConsumer.accept(new NamingFinishedRecipe(
                id,
                result,
                ingredient,
                pattern,
                ability
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
        private final String ability;

        public NamingFinishedRecipe(ResourceLocation id, ItemStack result, Ingredient ingredient, Pattern pattern,
                                    String ability) {
            this.id = id;
            this.result = result;
            this.ingredient = ingredient;
            this.pattern = pattern;
            this.ability = ability;
        }

        @Override
        public void serializeRecipeData(@Nonnull JsonObject pJson) {
            pJson.add("ingredient", ingredient.toJson());
            pJson.addProperty("pattern", pattern.pattern());
            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", Objects.requireNonNull(result.getItem().getRegistryName()).toString());
            if (result.getCount() > 1) {
                resultObject.addProperty("count", result.getCount());
            }
            if (result.getTag() != null) {
                resultObject.addProperty("nbt", result.getTag().toString());
            }
            pJson.add("result", resultObject);
            if (ability != null) {
                pJson.addProperty("ability", ability);
            }
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
