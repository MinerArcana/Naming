package com.minerarcana.naming.event;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.api.blockentity.NameableBlockEntityWrapper;
import com.minerarcana.naming.blockentity.NameableBlockRegistry;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingKeyBindings;
import com.minerarcana.naming.screen.NamerScreen;
import com.minerarcana.naming.target.BlockEntityNamingTarget;
import com.minerarcana.naming.target.EmptyTarget;
import com.minerarcana.naming.target.EntityNamingTarget;
import com.minerarcana.naming.target.ItemStackNamingTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

@EventBusSubscriber(modid = Naming.ID, value = Dist.CLIENT)
public class ForgeClientEventHandler {
    @SubscribeEvent
    public static void keyInputEvent(InputEvent.Key event) {
        handleInputEvent(event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void mouseInputEvent(InputEvent.MouseButton event) {
        handleInputEvent(event.getButton(), event.getAction());
    }

    private static void handleInputEvent(int key, int action) {
        Minecraft minecraft = Minecraft.getInstance();
        if (key == NamingKeyBindings.NAME.getKey().getValue() && action == 1 && minecraft.player != null && minecraft.screen == null) {
            boolean hasAbility = minecraft.player.getCapability(Namer.CAP)
                    .map(cap -> cap.hasAbility("naming"))
                    .orElse(false);
            if (hasAbility) {
                HitResult result = minecraft.hitResult;
                if (result instanceof EntityHitResult entityHitResult && entityHitResult.getType() != Type.MISS) {
                    minecraft.setScreen(new NamerScreen(new EntityNamingTarget(((EntityHitResult) result).getEntity())));
                } else if (result instanceof BlockHitResult blockHitResult && blockHitResult.getType() != Type.MISS && minecraft.level != null) {
                    BlockEntity blockEntity = minecraft.level.getBlockEntity(blockHitResult.getBlockPos());
                    NameableBlockEntityWrapper<?> wrapper = NameableBlockRegistry.getInstance()
                            .getNameableWrapperFor(blockEntity);
                    if (wrapper != null) {
                        minecraft.setScreen(new NamerScreen(new BlockEntityNamingTarget(wrapper)));
                    }
                } else if (result == null || result.getType() == Type.MISS) {
                    Iterator<InteractionHand> hands = Arrays.stream(InteractionHand.values()).iterator();
                    Screen screen = null;
                    while (screen == null && hands.hasNext()) {
                        InteractionHand hand = hands.next();
                        ItemStack itemStack = minecraft.player.getItemInHand(hand);
                        if (!itemStack.isEmpty()) {
                            screen = new NamerScreen(new ItemStackNamingTarget(itemStack, hand));
                        }
                    }
                    minecraft.setScreen(Objects.requireNonNullElseGet(screen, () -> new NamerScreen(new EmptyTarget())));
                }
            }
        }
    }
}
