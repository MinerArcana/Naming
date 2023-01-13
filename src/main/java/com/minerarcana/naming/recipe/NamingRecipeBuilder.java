package com.minerarcana.naming.recipe;

import com.google.gson.JsonObject;
import com.minerarcana.naming.content.NamingRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class NamingRecipeBuilder {
    private final ItemStack result;
    private Ingredient ingredient;
    private Pattern pattern;
    private String patternExample;
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

    public NamingRecipeBuilder withPatternExample(String patternExample) {
        this.patternExample = patternExample;
        return this;
    }

    public NamingRecipeBuilder withPattern(Pattern pattern) {
        this.pattern = pattern;
        return this;
    }

    public void build(Consumer<FinishedRecipe> recipeConsumer) {
        this.build(recipeConsumer, ForgeRegistries.ITEMS.getKey(result.getItem()));
    }

    public void build(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation id) {
        Objects.requireNonNull(result, "Result cannot be null");
        Objects.requireNonNull(ingredient, "Ingredient cannot be null");
        Objects.requireNonNull(pattern, "Pattern cannot be null");

        recipeConsumer.accept(new NamingFinishedRecipe(
                id,
                result,
                ingredient,
                pattern,
                patternExample,
                ability
        ));
    }

    public static NamingRecipeBuilder of(ItemLike item) {
        return new NamingRecipeBuilder(new ItemStack(item));
    }

    public static class NamingFinishedRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack result;
        private final Ingredient ingredient;
        private final Pattern pattern;
        private final String patternExample;
        private final String ability;

        public NamingFinishedRecipe(ResourceLocation id, ItemStack result, Ingredient ingredient, Pattern pattern,
                                    String patternExample, String ability) {
            this.id = id;
            this.result = result;
            this.ingredient = ingredient;
            this.pattern = pattern;
            this.patternExample = patternExample;
            this.ability = ability;
        }

        @Override
        public void serializeRecipeData(@Nonnull JsonObject pJson) {
            pJson.add("ingredient", ingredient.toJson());
            pJson.addProperty("pattern", pattern.pattern());
            if (this.patternExample != null) {
                pJson.addProperty("example", this.patternExample);
            }
            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.getItem())).toString());
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
        public RecipeSerializer<?> getType() {
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
