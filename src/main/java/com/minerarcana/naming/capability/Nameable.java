package com.minerarcana.naming.capability;

import com.minerarcana.naming.api.capability.INameable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.UUID;

public class Nameable implements INameable, INBTSerializable<CompoundTag> {
    public static Capability<INameable> CAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    private boolean magicallyNamed = false;
    private UUID namerUUID = null;

    @Override
    public boolean isMagicallyNamed() {
        return this.magicallyNamed;
    }

    @Override
    @Nonnull
    public UUID getNamerUUID() {
        return namerUUID;
    }

    @Override
    public void setMagicallyNamedBy(@Nonnull Entity entity) {
        this.magicallyNamed = true;
        this.namerUUID = entity.getUUID();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        if (this.namerUUID != null) {
            compoundTag.putUUID("NamerUUID", this.namerUUID);
        }
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("NamerUUID")) {
            this.magicallyNamed = true;
            this.namerUUID = nbt.getUUID("NamerUUID");
        } else {
            this.magicallyNamed = false;
        }
    }
}
