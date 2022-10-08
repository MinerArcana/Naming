package com.minerarcana.naming.capability;

import com.minerarcana.naming.api.capability.INameable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NameableCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    private final Nameable nameable;
    private final LazyOptional<INameable> lazy;

    public NameableCapabilityProvider() {
        this.nameable = new Nameable();
        this.lazy = LazyOptional.of(() -> nameable);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return Nameable.CAP.orEmpty(cap, lazy);
    }

    @Override
    public CompoundTag serializeNBT() {
        return nameable.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nameable.deserializeNBT(nbt);
    }
}
