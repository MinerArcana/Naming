package com.minerarcana.naming.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.regex.Pattern;

public class NamingRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<NamingRecipe> {
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public NamingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
        return new NamingRecipe(
                id,
                Ingredient.fromJson(jsonObject.get("ingredient")),
                Pattern.compile(JSONUtils.getAsString(jsonObject, "pattern")),
                CraftingHelper.getItemStack(jsonObject.getAsJsonObject("result"), true)
        );
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public NamingRecipe fromNetwork(ResourceLocation id, PacketBuffer packetBuffer) {
        return new NamingRecipe(
                id,
                Ingredient.fromNetwork(packetBuffer),
                Pattern.compile(packetBuffer.readUtf()),
                packetBuffer.readItem()
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void toNetwork(PacketBuffer packetBuffer, NamingRecipe namingRecipe) {
        namingRecipe.getIngredient().toNetwork(packetBuffer);
        packetBuffer.writeUtf(namingRecipe.getPattern().pattern());
        packetBuffer.writeItemStack(namingRecipe.getResultItem(), false);
    }
}
