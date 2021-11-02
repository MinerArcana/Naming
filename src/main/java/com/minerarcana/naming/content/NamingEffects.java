package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.DyeColor;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class NamingEffects {
    public static final RegistryEntry<Effect> HOARSE = Naming.getRegistrate()
            .object("hoarse")
            .simple(Effect.class, () -> new Effect(EffectType.HARMFUL, DyeColor.RED.getTextColor()) {
            });

    public static void setup() {

    }
}
