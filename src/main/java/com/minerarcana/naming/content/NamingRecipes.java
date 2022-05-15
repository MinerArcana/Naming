package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.recipe.NamingRecipe;
import com.minerarcana.naming.recipe.NamingRecipeSerializer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class NamingRecipes {
    public static RecipeType<NamingRecipe> NAMING_RECIPE_TYPE;

    public static final RegistryEntry<NamingRecipeSerializer> NAMING_RECIPE_SERIALIZER = Naming.getRegistrate()
            .object("naming")
            .simple(RecipeSerializer.class, NamingRecipeSerializer::new);

    public static void setup() {
        Naming.getRegistrate()
                .addRegisterCallback(RecipeType.class, () -> NAMING_RECIPE_TYPE = RecipeType.register("naming:naming"));
    }
}
