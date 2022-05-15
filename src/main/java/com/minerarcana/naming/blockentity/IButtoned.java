package com.minerarcana.naming.blockentity;

import net.minecraft.network.chat.Component;

public interface IButtoned<T extends Enum<T>> {
    Component getMessage();

    T cycle();
}
