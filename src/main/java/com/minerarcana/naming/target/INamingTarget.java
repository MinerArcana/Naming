package com.minerarcana.naming.target;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface INamingTarget {
    boolean isValid(@Nullable Entity namer);

    void name(@Nonnull String name, @Nullable Entity namer);

    @Nullable
    String getName();

    void toPacketBuffer(FriendlyByteBuf buffer);

    void hydrate(ServerLevel serverWorld);

    @Nonnull
    NamingTargetType getType();

    Object getTarget();
}
