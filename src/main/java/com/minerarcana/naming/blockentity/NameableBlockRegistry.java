package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.api.blockentity.NameableBlockEntityWrapper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.*;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NameableBlockRegistry {
    private static final NameableBlockRegistry INSTANCE = new NameableBlockRegistry();
    private final Map<BlockEntityType<?>, Function<BlockEntity, NameableBlockEntityWrapper<?>>> nameableBlocks;

    public NameableBlockRegistry() {
        nameableBlocks = new IdentityHashMap<>();
        this.setup();
    }

    public void add(BlockEntityType<?> blockEntityType, Function<BlockEntity, NameableBlockEntityWrapper<? extends BlockEntity>> wrapperCreator) {
        this.nameableBlocks.put(blockEntityType, wrapperCreator);
    }

    public boolean contains(BlockEntity blockEntity) {
        return nameableBlocks.containsKey(blockEntity.getType()) || blockEntity instanceof BaseContainerBlockEntity;
    }

    @Nullable
    public NameableBlockEntityWrapper<?> getNameableWrapperFor(@Nullable BlockEntity blockEntity) {
        if (blockEntity != null) {
            if (nameableBlocks.containsKey(blockEntity.getType())) {
                return Optional.ofNullable(nameableBlocks.get(blockEntity.getType()))
                        .map(naming -> naming.apply(blockEntity))
                        .orElse(null);
            } else if (blockEntity instanceof BaseContainerBlockEntity containerBlockEntity) {
                return NameableBlockEntityWrapper.forBaseContainer(containerBlockEntity);
            }
        }


        return null;
    }

    public void setup() {
        this.add(BlockEntityType.ENCHANTING_TABLE, forBlockEntity(
                EnchantmentTableBlockEntity.class,
                EnchantmentTableBlockEntity::setCustomName
        ));
        this.add(BlockEntityType.BANNER, forBlockEntity(
                BannerBlockEntity.class,
                BannerBlockEntity::setCustomName
        ));
    }

    public static <T extends BlockEntity & Nameable> Function<BlockEntity, NameableBlockEntityWrapper<?>> forBlockEntity(
            Class<T> tClass,
            BiConsumer<T, Component> setCustomName
    ) {
        return forBlockEntity(
                tClass,
                setCustomName,
                value -> value.getBlockState().getBlock().getName()
        );
    }

    public static <T extends BlockEntity & Nameable> Function<BlockEntity, NameableBlockEntityWrapper<?>> forBlockEntity(
            Class<T> tClass,
            BiConsumer<T, Component> setCustomName,
            Function<T, Component> getOriginalName
    ) {
        return blockEntity -> NameableBlockEntityWrapper.forClass(blockEntity, tClass, setCustomName, getOriginalName);
    }

    public static NameableBlockRegistry getInstance() {
        return INSTANCE;
    }
}
