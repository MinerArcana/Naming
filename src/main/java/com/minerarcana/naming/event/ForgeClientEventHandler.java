package com.minerarcana.naming.event;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.content.NamingKeyBindings;
import com.minerarcana.naming.screen.NamerScreen;
import com.minerarcana.naming.target.EntityNamingTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

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
                }
            }
        }
    }
}
