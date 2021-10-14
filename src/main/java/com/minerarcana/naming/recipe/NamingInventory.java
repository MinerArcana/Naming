package com.minerarcana.naming.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class NamingInventory extends Inventory {
    private final String name;

    public NamingInventory(String name, ItemStack input) {
        super(input);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
