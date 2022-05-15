package com.minerarcana.naming.blockentity;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;

import java.util.function.Function;

public interface ISideText {
    boolean renderSide(Direction side);

    DyeColor getTextColor();

    FormattedCharSequence getRenderedMessage(int index, Function<Component, FormattedCharSequence> generate);
}
