package com.minerarcana.naming.target;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

public class EntityNamingTarget implements INamingTarget {
    private final int id;
    private WeakReference<Entity> weakEntity;

    public EntityNamingTarget(Entity entity) {
        this.id = entity != null ? entity.getId() : -1;
        this.weakEntity = new WeakReference<>(entity);
    }

    public EntityNamingTarget(int id) {
        this.id = id;
        this.weakEntity = new WeakReference<>(null);
    }

    @Override
    public boolean isValid(@Nullable Entity namer) {
        Entity entity = weakEntity.get();
        return entity != null && entity.isAlive() && (namer == null || entity.distanceTo(namer) <= 196);
    }

    @Override
    public void name(@Nonnull String name) {
        Entity entity = weakEntity.get();
        if (entity != null) {
            entity.setCustomName(new StringTextComponent(name));
        }
    }

    @Override
    @Nullable
    public String getName() {
        Entity entity = this.weakEntity.get();
        if (entity != null) {
            return entity.getName().getContents();
        } else {
            return null;
        }
    }

    @Override
    public void hydrate(ServerWorld serverWorld) {
        this.weakEntity = new WeakReference<>(serverWorld.getEntity(id));
    }

    @Override
    public NamingTargetType getType() {
        return NamingTargets.ENTITY;
    }

    @Override
    public Object getTarget() {
        return weakEntity.get();
    }

    public void toPacketBuffer(PacketBuffer buffer) {
        buffer.writeInt(id);
    }

    public static EntityNamingTarget fromPacketBuffer(PacketBuffer buffer) {
        return new EntityNamingTarget(buffer.readInt());
    }
}
