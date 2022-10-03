package com.minerarcana.naming.compat.jei;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.content.NamingRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;

@JeiPlugin
public class NamingJEI implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = Naming.rl("jei");

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new NamingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        registration.addRecipes(
                NamingCategory.JEI_RECIPE_TYPE,
                Objects.requireNonNull(Minecraft.getInstance().level)
                        .getRecipeManager()
                        .getAllRecipesFor(NamingRecipes.NAMING_RECIPE_TYPE.get())
        );
    }
}
