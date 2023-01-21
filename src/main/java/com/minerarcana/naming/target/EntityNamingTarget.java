package com.minerarcana.naming.target;

import com.minerarcana.naming.capability.Nameable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public void name(@Nullable String name, @NotNull Entity namer) {
        Entity entity = weakEntity.get();
        if (entity != null) {
            if (name != null) {
                entity.setCustomName(Component.literal(name));
                if (entity instanceof Mob) {
                    ((Mob) entity).setPersistenceRequired();
                }
                entity.getCapability(Nameable.CAP)
                        .ifPresent(nameable -> nameable.setMagicallyNamedBy(namer));
            } else {
                entity.setCustomName(null);
            }
        }
    }

    @Override
    @Nullable
    public String getName() {
        Entity entity = this.weakEntity.get();
        if (entity != null) {
            return entity.getName().getString();
        } else {
            return null;
        }
    }

    @Override
    public void hydrate(ServerLevel serverWorld) {
        this.weakEntity = new WeakReference<>(serverWorld.getEntity(id));
    }

    @Override
    @NotNull
    public NamingTargetType getType() {
        return NamingTargets.ENTITY;
    }

    @Override
    public Object getTarget() {
        return weakEntity.get();
    }

    @Override
    public boolean matchesOriginal(String value) {
        Entity entity = this.weakEntity.get();
        return entity != null && entity.getType().getDescription().getString().equals(value);
    }

    public void toPacketBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt(id);
    }

    public static EntityNamingTarget fromPacketBuffer(FriendlyByteBuf buffer) {
        return new EntityNamingTarget(buffer.readInt());
    }
}
