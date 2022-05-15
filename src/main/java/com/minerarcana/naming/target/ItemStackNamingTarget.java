package com.minerarcana.naming.target;

import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingRecipes;
import com.minerarcana.naming.recipe.NamingInventory;
import com.minerarcana.naming.recipe.NamingRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ItemStackNamingTarget implements INamingTarget {
    private final ItemStack itemStack;
    private final InteractionHand hand;

    public ItemStackNamingTarget(ItemStack itemStack, InteractionHand hand) {
        this.itemStack = itemStack;
        this.hand = hand;
    }

    @Override
    public boolean isValid(@Nullable Entity namer) {
        if (namer instanceof LivingEntity) {
            return ItemStack.isSame(itemStack, ((LivingEntity) namer).getItemInHand(hand));
        }
        return true;
    }

    @Override
    public void name(@Nonnull String name, Entity namer) {
        if (namer instanceof LivingEntity livingNamer) {
            NamingInventory namingInventory = new NamingInventory(
                    name,
                    livingNamer.getItemInHand(hand),
                    livingNamer.getCapability(Namer.CAP)
            );
            Optional<NamingRecipe> recipeOptional = livingNamer.level.getRecipeManager()
                    .getRecipeFor(
                            NamingRecipes.NAMING_RECIPE_TYPE,
                            namingInventory,
                            livingNamer.level
                    );
            ItemStack inputStack = livingNamer.getItemInHand(hand);
            if (recipeOptional.isPresent()) {
                inputStack.shrink(1);
                ItemStack result = recipeOptional.get().assemble(namingInventory);
                if (inputStack.isEmpty()) {
                    livingNamer.setItemInHand(hand, result);
                } else if (livingNamer instanceof Player) {
                    ItemHandlerHelper.giveItemToPlayer((Player) livingNamer, result);
                } else {
                    livingNamer.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                            .map(itemHandler -> ItemHandlerHelper.insertItem(itemHandler, result, false))
                            .filter(insertResult -> !insertResult.isEmpty())
                            .ifPresent(ignore -> inputStack.grow(1));
                }
            } else {
                if (!inputStack.isEmpty()) {
                    inputStack.setHoverName(new TextComponent(name));
                }
            }
        }
    }

    @Nullable
    @Override
    public String getName() {
        return itemStack.getHoverName().getString();
    }

    @Override
    public void toPacketBuffer(FriendlyByteBuf buffer) {
        buffer.writeItemStack(itemStack, false);
        buffer.writeEnum(hand);
    }

    @Override
    public void hydrate(ServerLevel serverWorld) {

    }

    @Override
    @Nonnull
    public NamingTargetType getType() {
        return NamingTargets.ITEMSTACK;
    }

    @Override
    public Object getTarget() {
        return itemStack;
    }

    public static ItemStackNamingTarget fromPacketBuffer(FriendlyByteBuf packetBuffer) {
        return new ItemStackNamingTarget(
                packetBuffer.readItem(),
                packetBuffer.readEnum(InteractionHand.class)
        );
    }
}
