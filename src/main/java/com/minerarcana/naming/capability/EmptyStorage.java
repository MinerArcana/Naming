package com.minerarcana.naming.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class EmptyStorage<T> implements Capability.IStorage<T> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
        return new CompoundNBT();
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {

    }
}
