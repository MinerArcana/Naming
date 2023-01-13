package com.minerarcana.naming;

import com.minerarcana.naming.api.capability.INameable;
import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.content.*;
import com.minerarcana.naming.network.NetworkHandler;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.brassgoggledcoders.shadyskies.containersyncing.ContainerSyncing;

@Mod(Naming.ID)
public class Naming {
    public static final String ID = "naming";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    private static final Lazy<Registrate> REGISTRATE = Lazy.of(() -> Registrate.create(ID)
            .addDataGenerator(ProviderType.ADVANCEMENT, NamingAdvancements::generateAdvancements)
    );

    public static ContainerSyncing containerSyncing;
    public static NetworkHandler network;

    public Naming() {
        IEventBus modBus = FMLJavaModLoadingContext.get()
                .getModEventBus();
        
        NamingCriteriaTriggers.setup();
        NamingText.setup();
        NamingBlocks.setup();
        NamingRecipes.setup();
        NamingContainers.setup();
        NamingEffects.setup();

        modBus.addListener(this::registerCapabilities);
        containerSyncing = ContainerSyncing.setup(ID, LOGGER);
        network = new NetworkHandler();
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(INamer.class);
        event.register(INameable.class);
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
