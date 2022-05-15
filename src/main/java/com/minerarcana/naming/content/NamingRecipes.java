package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.recipe.NamingRecipe;
import com.minerarcana.naming.recipe.NamingRecipeSerializer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class NamingRecipes {
    public static RegistryObject<RecipeType<NamingRecipe>> NAMING_RECIPE_TYPE = RegistryObject.create(
            Naming.rl("naming"),
            Registry.RECIPE_TYPE_REGISTRY,
            Naming.ID
    );

    public static final RegistryEntry<NamingRecipeSerializer> NAMING_RECIPE_SERIALIZER = Naming.getRegistrate()
            .object("naming")
            .simple(RecipeSerializer.class, NamingRecipeSerializer::new);

    public static void setup(IEventBus bus) {
        bus.addGenericListener(RecipeSerializer.class, NamingRecipes::registerRecipe);
    }

    public static void registerRecipe(RegistryEvent<RecipeSerializer<?>> event) {
        RecipeType.register("naming:naming");
    }
}
