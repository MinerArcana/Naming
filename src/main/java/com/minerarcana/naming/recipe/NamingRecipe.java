package com.minerarcana.naming.recipe;

import com.minerarcana.naming.content.NamingRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.regex.Pattern;

public class NamingRecipe implements IRecipe<NamingInventory> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final Pattern pattern;
    private final ItemStack result;

    public NamingRecipe(ResourceLocation id, Ingredient ingredient, Pattern pattern, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.pattern = pattern;
        this.result = result;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(NamingInventory namingInventory, World level) {
        return this.ingredient.test(namingInventory.getItem(0)) &&
                this.pattern.matcher(namingInventory.getName()).matches();
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
    public IRecipeSerializer<?> getSerializer() {
        return NamingRecipes.NAMING_RECIPE_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return NamingRecipes.NAMING_RECIPE_TYPE;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
