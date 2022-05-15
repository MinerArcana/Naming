package com.minerarcana.naming.capability;

import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NamingCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    private final Namer namer;
    private final LazyOptional<INamer> lazy;

    public NamingCapabilityProvider() {
        this.namer = new Namer();
        this.lazy = LazyOptional.of(() -> namer);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return Namer.CAP.orEmpty(cap, lazy);
    }

    @Override
    public CompoundTag serializeNBT() {
        return namer.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        namer.deserializeNBT(nbt);
    }

    public void invalidate() {
        lazy.invalidate();
    }
}
