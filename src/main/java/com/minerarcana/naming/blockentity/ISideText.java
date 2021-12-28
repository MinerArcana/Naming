package com.minerarcana.naming.blockentity;

import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Function;

public interface ISideText {
    boolean renderSide(Direction side);

    DyeColor getTextColor();

    IReorderingProcessor getRenderedMessage(int index, Function<ITextComponent, IReorderingProcessor> generate);
}
