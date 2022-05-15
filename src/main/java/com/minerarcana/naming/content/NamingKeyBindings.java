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
    private static final String CATEGORY = "key.category.naming";

    public static final KeyMapping NAME = new KeyMapping(
            "key.naming.name",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            CATEGORY
    );

    @SubscribeEvent
    public static void registerKeyBinding(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(NAME);
    }
}
