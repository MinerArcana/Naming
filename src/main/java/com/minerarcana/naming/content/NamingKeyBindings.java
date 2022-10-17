package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Naming.ID, value = Dist.CLIENT, bus = Bus.MOD)
public class NamingKeyBindings {

    public static final KeyMapping NAME = new KeyMapping(
            NamingText.KEY_NAMING.getKey(),
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            NamingText.KEY_CATEGORY.getKey()
    );

    public static final KeyMapping RADIAL = new KeyMapping(
            NamingText.KEY_RADIAL.getKey(),
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            NamingText.KEY_CATEGORY.getKey()
    );

    @SubscribeEvent
    public static void registerKeyBinding(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(NAME);
        ClientRegistry.registerKeyBinding(RADIAL);
    }
}
