package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.DyeColor;

public class NamingEffects {
    public static final RegistryEntry<MobEffect> HOARSE = Naming.getRegistrate()
            .object("hoarse")
            .simple(Registry.MOB_EFFECT_REGISTRY, () -> new MobEffect(MobEffectCategory.HARMFUL, DyeColor.RED.getTextColor()) {
            });

    public static void setup() {

    }
}
