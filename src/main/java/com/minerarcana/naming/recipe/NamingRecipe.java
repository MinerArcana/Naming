package com.minerarcana.naming.recipe;

import com.minerarcana.naming.content.NamingRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.regex.Pattern;

public class NamingRecipe implements Recipe<NamingInventory> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final Pattern pattern;
    private final String patternExample;
    private final ItemStack result;
    private final String ability;

    public NamingRecipe(ResourceLocation id, Ingredient ingredient, Pattern pattern, String patternExample, ItemStack result, String ability) {
        this.id = id;
        this.ingredient = ingredient;
        this.pattern = pattern;
        if (patternExample == null) {
            this.patternExample = pattern.pattern();
        } else {
            this.patternExample = patternExample;
        }
        this.result = result;
        this.ability = ability;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(NamingInventory namingInventory, Level level) {
        return this.ingredient.test(namingInventory.getItem(0)) &&
                this.pattern.matcher(namingInventory.getName()).matches() &&
                (this.ability.isEmpty() || namingInventory.hasAbility(this.ability));
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ItemStack assemble(NamingInventory namingInventory) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return NamingRecipes.NAMING_RECIPE_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return NamingRecipes.NAMING_RECIPE_TYPE.get();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getAbility() {
        return ability;
    }

    public String getPatternExample() {
        return this.patternExample;
    }
}
