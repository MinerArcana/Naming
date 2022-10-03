package com.minerarcana.naming.compat.jei;

import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingRecipes;
import com.minerarcana.naming.content.NamingText;
import com.minerarcana.naming.recipe.NamingRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Optional;

public class NamingCategory implements IRecipeCategory<NamingRecipe> {
    public static RecipeType<NamingRecipe> JEI_RECIPE_TYPE = new RecipeType<>(
            NamingRecipes.NAMING_RECIPE_TYPE.getId(),
            NamingRecipe.class
    );

    private final IDrawable icon;
    private final IDrawable background;

    public NamingCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.NAME_TAG));
        background = guiHelper.drawableBuilder(new ResourceLocation("jei:textures/gui/gui_vanilla.png"), 49, 168, 76, 18)
                .addPadding(14, 14, 50, 50)
                .build();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setRecipe(IRecipeLayoutBuilder builder, NamingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 51, 15)
                .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getIngredient().getItems()))
                .setSlotName("input");
        builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 15)
                .addItemStack(recipe.getResultItem())
                .setSlotName("result");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void draw(NamingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        Component speak = new TranslatableComponent(NamingText.JEI_SPEAK.getKey(), recipe.getPatternExample());
        drawText(minecraft, poseStack, speak, 0xFF80FF20, 4);

        boolean lacksAbility = Optional.ofNullable(minecraft.player)
                .flatMap(player -> player.getCapability(Namer.CAP)
                        .resolve()
                )
                .filter(namer -> namer.hasAbility(recipe.getAbility()))
                .isEmpty();
        if (lacksAbility) {
            drawText(minecraft, poseStack, NamingText.MISSING_ABILITY, 0xFFFF6060, 37);
        }
    }

    private void drawText(Minecraft minecraft, PoseStack poseStack, Component text, int mainColor, int y) {
        int shadowColor = 0xFF000000 | (mainColor & 0xFCFCFC) >> 2;
        int width = minecraft.font.width(text);
        int x = (background.getWidth() - width) / 2;

        // TODO 1.13 match the new GuiRepair style
        minecraft.font.draw(poseStack, text, x + 1, y, shadowColor);
        minecraft.font.draw(poseStack, text, x, y + 1, shadowColor);
        minecraft.font.draw(poseStack, text, x + 1, y + 1, shadowColor);
        minecraft.font.draw(poseStack, text, x, y, mainColor);
    }

    @Override
    @NotNull
    public Component getTitle() {
        return NamingText.SCREEN_TITLE;
    }

    @Override
    @NotNull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @NotNull
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    @NotNull
    @SuppressWarnings("removal")
    public ResourceLocation getUid() {
        return JEI_RECIPE_TYPE.getUid();
    }

    @Override
    @NotNull
    @SuppressWarnings("removal")
    public Class<? extends NamingRecipe> getRecipeClass() {
        return JEI_RECIPE_TYPE.getRecipeClass();
    }

    @Override
    @NotNull
    public RecipeType<NamingRecipe> getRecipeType() {
        return JEI_RECIPE_TYPE;
    }
}
