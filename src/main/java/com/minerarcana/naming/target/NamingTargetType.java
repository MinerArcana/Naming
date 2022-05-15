package com.minerarcana.naming.target;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public record NamingTargetType(String name, Function<FriendlyByteBuf, ? extends INamingTarget> creator) {
}
