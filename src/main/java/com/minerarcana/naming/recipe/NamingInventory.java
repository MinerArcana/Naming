package com.minerarcana.naming.recipe;

import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

public class NamingInventory extends SimpleContainer {
    private final String name;
    private final LazyOptional<INamer> namer;

    public NamingInventory(String name, ItemStack input, LazyOptional<INamer> namer) {
        super(input);
        this.name = name;
        this.namer = namer;
    }

    public String getName() {
        return name;
    }

    public boolean hasAbility(String ability) {
        return namer.map(value -> value.hasAbility(ability))
                .orElse(false);
    }
}
