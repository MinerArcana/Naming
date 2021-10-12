package com.minerarcana.naming.capability;

import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NamingCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
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
    public CompoundNBT serializeNBT() {
        return namer.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        namer.deserializeNBT(nbt);
    }

    public void invalidate() {
        lazy.invalidate();
    }
}
