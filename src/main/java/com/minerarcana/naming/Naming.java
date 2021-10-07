package com.minerarcana.naming;

import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.capability.EmptyStorage;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingAdvancements;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLanguageProvider;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;

@Mod(Naming.ID)
public class Naming {
    public static final String ID = "naming";

    private static final Lazy<Registrate> REGISTRATE = Lazy.of(() -> Registrate.create(ID)
            .addDataGenerator(ProviderType.ADVANCEMENT, NamingAdvancements::generateAdvancements)
    );

    public Naming() {
        NamingCriteriaTriggers.setup();
        REGISTRATE.get();

        FMLJavaModLoadingContext.get()
                .getModEventBus()
                .addListener(this::commonSetup);
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
