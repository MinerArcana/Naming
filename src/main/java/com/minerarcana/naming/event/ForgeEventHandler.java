package com.minerarcana.naming.event;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.content.NamingKeyBindings;
import com.minerarcana.naming.screen.NamingScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Naming.ID)
public class ForgeEventHandler {
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
                minecraft.setScreen(new NamingScreen());
            }
        }
    }
}
