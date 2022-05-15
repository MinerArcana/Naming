package com.minerarcana.naming.blockentity;

import net.minecraft.util.text.ITextComponent;

import ITextComponent;

public interface IButtoned<T extends Enum<T>> {
    ITextComponent getMessage();

    T cycle();
}
