package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import KeyBinding;

@EventBusSubscriber(modid = Naming.ID, value = Dist.CLIENT, bus = Bus.MOD)
public class NamingKeyBindings {
    private static final String CATEGORY = "key.category.naming";

    public static final KeyBinding NAME = new KeyBinding(
            "key.naming.name",
            KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM,
            -1,
            CATEGORY
    );

    @SubscribeEvent
    public static void registerKeyBinding(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(NAME);
    }
}
