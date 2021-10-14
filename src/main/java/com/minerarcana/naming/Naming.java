package com.minerarcana.naming;

import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.capability.EmptyStorage;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.*;
import com.minerarcana.naming.network.NetworkHandler;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Naming.ID)
public class Naming {
    public static final String ID = "naming";

    private static final Lazy<Registrate> REGISTRATE = Lazy.of(() -> Registrate.create(ID)
            .addDataGenerator(ProviderType.ADVANCEMENT, NamingAdvancements::generateAdvancements)
    );

    public static NetworkHandler network;

    public Naming() {
        NamingCriteriaTriggers.setup();
        NamingText.setup();
        NamingBlocks.setup();
        NamingRecipes.setup();

        IEventBus modBus = FMLJavaModLoadingContext.get()
                .getModEventBus();

        modBus.addListener(this::commonSetup);

        network = new NetworkHandler();
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(INamer.class, new EmptyStorage<>(), Namer::new);
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
