package com.minerarcana.naming.target;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface INamingTarget {
    boolean isValid(@Nullable Entity namer);

    void name(@Nonnull String name, @Nullable Entity namer);

    @Nullable
    String getName();

    void toPacketBuffer(PacketBuffer buffer);

    void hydrate(ServerWorld serverWorld);

    @Nonnull
    NamingTargetType getType();

    Object getTarget();
}
