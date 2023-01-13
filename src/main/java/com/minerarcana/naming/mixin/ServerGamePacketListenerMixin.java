package com.minerarcana.naming.mixin;

import com.minerarcana.naming.content.NamingCriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerMixin {
    @Shadow
    public ServerPlayer player;

    @Inject(
            method = "signBook",
            at = @At(
                    value = "TAIL"
            )
    )
    private void namingSignBook(FilteredText title, List<FilteredText> contents, int slot, CallbackInfo callbackInfo) {
        ItemStack signedItemStack = player.getInventory().getItem(slot);
        NamingCriteriaTriggers.SIGNING.trigger(player, signedItemStack, title.raw());
    }
}