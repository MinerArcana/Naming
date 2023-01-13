package com.minerarcana.naming.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.regex.Pattern;

public class NamingRecipeSerializer implements RecipeSerializer<NamingRecipe> {
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public NamingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
        return new NamingRecipe(
                id,
                Ingredient.fromJson(jsonObject.get("ingredient")),
                Pattern.compile(GsonHelper.getAsString(jsonObject, "pattern")),
                GsonHelper.getAsString(jsonObject, "example", null),
                CraftingHelper.getItemStack(jsonObject.getAsJsonObject("result"), true),
                GsonHelper.getAsString(jsonObject, "ability", "")
        );
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public NamingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf packetBuffer) {
        return new NamingRecipe(
                id,
                Ingredient.fromNetwork(packetBuffer),
                Pattern.compile(packetBuffer.readUtf()),
                packetBuffer.readUtf(),
                packetBuffer.readItem(),
                packetBuffer.readUtf()
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void toNetwork(FriendlyByteBuf packetBuffer, NamingRecipe namingRecipe) {
        namingRecipe.getIngredient().toNetwork(packetBuffer);
        packetBuffer.writeUtf(namingRecipe.getPattern().pattern());
        packetBuffer.writeUtf(namingRecipe.getPatternExample());
        packetBuffer.writeItemStack(namingRecipe.getResultItem(), false);
        packetBuffer.writeUtf(namingRecipe.getAbility());
    }
}
