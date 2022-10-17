package com.minerarcana.naming.event;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingKeyBindings;
import com.minerarcana.naming.screen.IAllowMovement;
import com.minerarcana.naming.screen.ManualKeyboardInput;
import com.minerarcana.naming.screen.NamerScreen;
import com.minerarcana.naming.screen.radial.NamingRadialScreen;
import com.minerarcana.naming.target.EmptyTarget;
import com.minerarcana.naming.target.EntityNamingTarget;
import com.minerarcana.naming.target.ItemStackNamingTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

@EventBusSubscriber(modid = Naming.ID, value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void inputUpdate(MovementInputUpdateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (Minecraft.getInstance().screen instanceof IAllowMovement && Minecraft.getInstance().player != null) {
            ManualKeyboardInput.handle(minecraft.player, minecraft.options);
        }
    }

    @SubscribeEvent
    public static void keyInputEvent(InputEvent.KeyInputEvent event) {
        handleInputEvent(event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void mouseInputEvent(InputEvent.MouseInputEvent event) {
        handleInputEvent(event.getButton(), event.getAction());
    }

    private static void handleInputEvent(int key, int action) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.screen == null && action == 1) {
            if (key == NamingKeyBindings.NAME.getKey().getValue()) {
                openNamingScreen(minecraft, minecraft.player);
            } else if (key == NamingKeyBindings.RADIAL.getKey().getValue()) {
                openRadialScreen(minecraft, minecraft.player);
            }
        }
    }

    private static void openNamingScreen(@NotNull Minecraft minecraft, @NotNull Player player) {
        boolean hasAbility = player.getCapability(Namer.CAP)
                .map(cap -> cap.hasAbility("naming"))
                .orElse(false);
        if (hasAbility) {
            HitResult result = minecraft.hitResult;
            if (result instanceof EntityHitResult) {
                minecraft.setScreen(new NamerScreen(new EntityNamingTarget(((EntityHitResult) result).getEntity())));
            } else if (result == null || result.getType() == HitResult.Type.MISS) {
                Iterator<InteractionHand> hands = Arrays.stream(InteractionHand.values()).iterator();
                Screen screen = null;
                while (screen == null && hands.hasNext()) {
                    InteractionHand hand = hands.next();
                    ItemStack itemStack = player.getItemInHand(hand);
                    if (!itemStack.isEmpty()) {
                        screen = new NamerScreen(new ItemStackNamingTarget(itemStack, hand));
                    }
                }
                minecraft.setScreen(Objects.requireNonNullElseGet(screen, () -> new NamerScreen(new EmptyTarget())));
            }
        }
    }

    private static void openRadialScreen(@NotNull Minecraft minecraft, @NotNull Player player) {
        boolean hasAbility = player.getCapability(Namer.CAP)
                .map(cap -> cap.hasAbility("naming_radial"))
                .orElse(false);
        if (hasAbility) {
            minecraft.setScreen(new NamingRadialScreen());
        }
    }
}
