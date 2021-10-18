package com.minerarcana.naming.mixin;

import com.minerarcana.naming.content.NamingCriteriaTriggers;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.ServerPlayNetHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ServerPlayNetHandler.class)
public class ServerPlayNetHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(
            method = "signBook",
            at = @At(
                    value = "TAIL"
            )
    )
    private void namingSignBook(String title, List<String> contents, int slot, CallbackInfo callbackInfo) {
        ItemStack signedItemStack = player.inventory.getItem(slot);
        NamingCriteriaTriggers.SIGNING.trigger(player, signedItemStack, title);
    }
}