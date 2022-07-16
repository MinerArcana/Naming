package com.minerarcana.naming.api.capability;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface INameable {
    boolean isMagicallyNamed();

    @Nonnull
    UUID getNamerUUID();

    void setMagicallyNamedBy(@Nonnull Entity entity);
}
