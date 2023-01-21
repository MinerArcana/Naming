package com.minerarcana.naming.target;

import com.minerarcana.naming.api.blockentity.NameableBlockEntityWrapper;
import com.minerarcana.naming.blockentity.NameableBlockRegistry;
import com.minerarcana.naming.capability.Nameable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class BlockEntityNamingTarget implements INamingTarget {
    private final BlockPos blockPos;

    private WeakReference<NameableBlockEntityWrapper<?>> weakEntity;

    public BlockEntityNamingTarget(BlockPos blockPos) {
        this.blockPos = blockPos;
        this.weakEntity = new WeakReference<>(null);
    }

    public BlockEntityNamingTarget(NameableBlockEntityWrapper<?> nameableBlockEntityWrapper) {
        this.blockPos = nameableBlockEntityWrapper.getBlockEntity().getBlockPos();
        this.weakEntity = new WeakReference<>(nameableBlockEntityWrapper);
    }

    @Override
    public boolean isValid(@Nullable Entity namer) {
        NameableBlockEntityWrapper<?> nameableBlockEntity = this.getWrapper();
        return nameableBlockEntity != null && nameableBlockEntity.isValid();
    }

    @Override
    public void name(@Nullable String name, @NotNull Entity namer) {
        NameableBlockEntityWrapper<?> entity = this.getWrapper();
        if (entity != null && entity.canName(namer)) {
            if (name != null) {
                entity.setCustomName(Component.literal(name));
                entity.getBlockEntity()
                        .getCapability(Nameable.CAP)
                        .ifPresent(nameable -> nameable.setMagicallyNamedBy(namer));
            } else {
                entity.setCustomName(null);
            }
        }
    }

    @Nullable
    @Override
    public String getName() {
        NameableBlockEntityWrapper<?> entity = this.getWrapper();
        if (entity != null) {
            return entity.getName().getString();
        } else {
            return null;
        }
    }

    @Override
    public void toPacketBuffer(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
    }

    @Override
    public void hydrate(ServerLevel serverWorld) {
        NameableBlockEntityWrapper<?> nameableBlockEntityWrapper = null;
        BlockEntity blockEntity = serverWorld.getBlockEntity(blockPos);
        if (blockEntity != null) {
            nameableBlockEntityWrapper = NameableBlockRegistry.getInstance().getNameableWrapperFor(blockEntity);
        }
        this.weakEntity = new WeakReference<>(nameableBlockEntityWrapper);
    }

    @NotNull
    @Override
    public NamingTargetType getType() {
        return NamingTargets.BLOCK_ENTITY;
    }

    @Override
    public Object getTarget() {
        NameableBlockEntityWrapper<?> nameableBlockEntityWrapper = this.getWrapper();
        return nameableBlockEntityWrapper != null ? nameableBlockEntityWrapper.getBlockEntity() : null;
    }

    @Override
    public boolean matchesOriginal(String value) {
        NameableBlockEntityWrapper<?> wrapper = this.getWrapper();
        return wrapper != null && wrapper.getOriginalName().getString().equals(value);
    }

    public static BlockEntityNamingTarget fromPacketBuffer(FriendlyByteBuf buffer) {
        return new BlockEntityNamingTarget(buffer.readBlockPos());
    }

    private NameableBlockEntityWrapper<?> getWrapper() {
        NameableBlockEntityWrapper<?> wrapper = this.weakEntity.get();
        if (wrapper != null && wrapper.isValid()) {
            return wrapper;
        } else {
            return null;
        }
    }
}
