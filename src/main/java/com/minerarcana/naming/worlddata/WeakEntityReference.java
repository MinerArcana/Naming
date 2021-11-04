package com.minerarcana.naming.worlddata;

import net.minecraft.entity.Entity;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class WeakEntityReference {
    private final UUID entityUUID;

    private WeakReference<Entity> entityReference;

    public WeakEntityReference(UUID entityUUID) {
        this.entityUUID = entityUUID;
    }

    public WeakEntityReference(Entity entity) {
        this.entityReference = new WeakReference<>(entity);
        this.entityUUID = entity.getUUID();
    }

    @Nullable
    public Entity get(IWorld world) {
        if (entityReference.get() == null && world instanceof ServerWorld) {
            entityReference = new WeakReference<>(((ServerWorld) world).getEntity(entityUUID));
        }
        return entityReference.get();
    }
}
