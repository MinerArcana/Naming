package com.minerarcana.naming.target;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EmptyTarget implements INamingTarget {
    @Override
    public boolean isValid(@Nullable Entity namer) {
        return true;
    }

    @Override
    public void name(@Nonnull String name, @Nullable Entity namer) {

    }

    @Nullable
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void toPacketBuffer(PacketBuffer buffer) {

    }

    @Override
    public void hydrate(ServerWorld serverWorld) {

    }

    @Nonnull
    @Override
    public NamingTargetType getType() {
        return NamingTargets.EMPTY;
    }

    @Override
    public Object getTarget() {
        return null;
    }
}
