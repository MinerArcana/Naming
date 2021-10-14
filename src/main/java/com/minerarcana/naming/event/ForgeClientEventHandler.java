package com.minerarcana.naming.event;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.content.NamingKeyBindings;
import com.minerarcana.naming.screen.NamerScreen;
import com.minerarcana.naming.target.EntityNamingTarget;
import com.minerarcana.naming.target.ItemStackNamingTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Arrays;
import java.util.Iterator;

@EventBusSubscriber(modid = Naming.ID, value = Dist.CLIENT)
public class ForgeClientEventHandler {
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
        if (action == 1 && minecraft.player != null && minecraft.screen == null) {
            if (key == NamingKeyBindings.NAME.getKey().getValue()) {
                RayTraceResult result = minecraft.hitResult;
                if (result instanceof EntityRayTraceResult) {
                    minecraft.setScreen(new NamerScreen(new EntityNamingTarget(((EntityRayTraceResult) result).getEntity())));
                } else if (result == null || result.getType() == RayTraceResult.Type.MISS) {
                    Iterator<Hand> hands = Arrays.stream(Hand.values()).iterator();
                    Screen screen = null;
                    while (screen == null && hands.hasNext()) {
                        Hand hand = hands.next();
                        ItemStack itemStack = minecraft.player.getItemInHand(hand);
                        if (!itemStack.isEmpty()) {
                            screen = new NamerScreen(new ItemStackNamingTarget(itemStack, hand));
                        }
                    }
                    if (screen != null) {
                        minecraft.setScreen(screen);
                    }
                }
            }
        }
    }
}
