package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.recipe.NamingRecipe;
import com.minerarcana.naming.recipe.NamingRecipeSerializer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;

public class NamingRecipes {
    public static final IRecipeType<NamingRecipe> NAMING_RECIPE_TYPE = IRecipeType.register("naming:naming");

    public static final RegistryEntry<NamingRecipeSerializer> NAMING_RECIPE_SERIALIZER = Naming.getRegistrate()
            .object("naming")
            .simple(IRecipeSerializer.class, NamingRecipeSerializer::new);

    public static void setup() {

    }
}
