package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.recipe.NamingRecipe;
import com.minerarcana.naming.recipe.NamingRecipeSerializer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeType;

public class NamingRecipes {
    public static final RegistryEntry<NamingRecipeSerializer> NAMING_RECIPE_SERIALIZER = Naming.getRegistrate()
            .object("naming")
            .simple(Registry.RECIPE_SERIALIZER_REGISTRY, NamingRecipeSerializer::new);

    public static final RegistryEntry<RecipeType<NamingRecipe>> NAMING_RECIPE_TYPE = Naming.getRegistrate()
            .object("naming")
            .simple(Registry.RECIPE_TYPE_REGISTRY, () -> RecipeType.simple(Naming.rl("naming")));

    public static void setup() {
    }
}
