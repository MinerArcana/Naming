package com.minerarcana.naming.api.blockentity;

import com.minerarcana.naming.mixin.BaseContainerBlockEntityAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class NameableBlockEntityWrapper<T extends BlockEntity & Nameable> implements Nameable {
    private final T blockEntity;

    public NameableBlockEntityWrapper(T blockEntity) {
        this.blockEntity = blockEntity;
    }

    public T getBlockEntity() {
        return this.blockEntity;
    }

    public boolean isValid() {
        return !this.getBlockEntity().isRemoved();
    }

    @Override
    @NotNull
    public Component getName() {
        if (this.hasCustomName() && this.getCustomName() != null) {
            return this.getCustomName();
        }
        return this.getOriginalName();
    }

    @Override
    public boolean hasCustomName() {
        return this.getBlockEntity().hasCustomName();
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return this.getBlockEntity().getDisplayName();
    }

    @Nullable
    public Component getCustomName() {
        return this.getBlockEntity().getCustomName();
    }

    public abstract void setCustomName(Component component);

    public abstract Component getOriginalName();

    public static <T extends BaseContainerBlockEntity> NameableBlockEntityWrapper<T> forBaseContainer(T blockEntity) {
        return new NameableBlockEntityWrapper<>(blockEntity) {
            @Override
            public void setCustomName(Component component) {
                this.getBlockEntity()
                        .setCustomName(component);
            }

            @Override
            public Component getOriginalName() {
                return ((BaseContainerBlockEntityAccessor) this.getBlockEntity()).callGetDefaultName();
            }
        };
    }

    public static <T extends BlockEntity & Nameable> NameableBlockEntityWrapper<T> forClass(
            BlockEntity blockEntity, Class<T> tClass, BiConsumer<T, Component> setCustomName, Function<T, Component> getOriginalName
    ) {
        if (tClass.isInstance(blockEntity)) {
            return new NameableBlockEntityWrapper<>(tClass.cast(blockEntity)) {
                @Override
                public void setCustomName(Component component) {
                    setCustomName.accept(this.getBlockEntity(), component);
                }

                @Override
                public Component getOriginalName() {
                    return getOriginalName.apply(this.getBlockEntity());
                }
            };
        } else {
            return null;
        }
    }
}
