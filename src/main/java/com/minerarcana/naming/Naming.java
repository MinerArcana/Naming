package com.minerarcana.naming;

import com.minerarcana.naming.content.NamingAdvancements;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.common.Mod;

@Mod(Naming.ID)
public class Naming {
    public static final String ID = "naming";

    private static final Lazy<Registrate> REGISTRATE = Lazy.of(() -> Registrate.create(ID)
            .addDataGenerator(ProviderType.ADVANCEMENT, NamingAdvancements::generateAdvancements)
    );

    public Naming() {
        NamingCriteriaTriggers.setup();
        REGISTRATE.get();
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
