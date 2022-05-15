package com.minerarcana.naming.worlddata;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

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
    public Entity get(LevelAccessor world) {
        if (entityReference.get() == null && world instanceof ServerLevel) {
            entityReference = new WeakReference<>(((ServerLevel) world).getEntity(entityUUID));
        }
        return entityReference.get();
    }
}
