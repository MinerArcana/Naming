package com.minerarcana.naming.target;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


public class EmptyTarget implements INamingTarget {
    @Override
    public boolean isValid(@Nullable Entity namer) {
        return true;
    }

    @Override
    public void name(@Nullable String name, @NotNull Entity namer) {

    }

    @Nullable
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void toPacketBuffer(FriendlyByteBuf buffer) {

    }

    @Override
    public void hydrate(ServerLevel serverWorld) {

    }

    @NotNull
    @Override
    public NamingTargetType getType() {
        return NamingTargets.EMPTY;
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    public boolean matchesOriginal(String value) {
        return false;
    }
}
