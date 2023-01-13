package com.minerarcana.naming.container;

import com.google.common.collect.Lists;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.blockentity.IButtoned;
import com.minerarcana.naming.blockentity.MessageBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.IPropertyManaged;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.Property;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyManager;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class MessageContainer<T extends Enum<T> & IButtoned<T>> extends AbstractContainerMenu implements IPropertyManaged {
    private final ContainerLevelAccess callable;
    private final PropertyManager propertyManager;

    private final Property<String> name;
    private final List<Pair<Property<Integer>, Property<String>>> listeners;
    private final Inventory inventory;

    public MessageContainer(MenuType<? extends MessageContainer> type, int containerId, Inventory inventory,
                            MessageBlockEntity blockEntity, Function<Integer, T> getter,
                            BiConsumer<Integer, T> setting) {
        super(type, containerId);
        this.inventory = inventory;
        this.callable = ContainerLevelAccess.create(
                Objects.requireNonNull(blockEntity.getLevel()),
                blockEntity.getBlockPos()
        );
        this.propertyManager = Naming.containerSyncing.createManager(containerId);
        this.name = this.propertyManager.addTrackedProperty(PropertyTypes.STRING.create(
                blockEntity::getName,
                blockEntity::setName
        ));
        this.listeners = Lists.newArrayList();
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            listeners.add(Pair.of(
                    this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(
                            () -> getter.apply(finalI).ordinal(),
                            ordinal -> setting.accept(finalI, this.getEnum().getEnumConstants()[ordinal]))
                    ),
                    this.propertyManager.addTrackedProperty(PropertyTypes.STRING.create(
                            () -> blockEntity.getMessage(finalI).getString(),
                            message -> blockEntity.setMessage(finalI, Component.literal(message)))
                    )
            ));
        }
    }

    @SuppressWarnings("unused")
    public MessageContainer(@Nullable MenuType<?> type, int containerId, Inventory playerInventory) {
        super(type, containerId);
        this.inventory = playerInventory;
        this.callable = ContainerLevelAccess.NULL;
        this.propertyManager = Naming.containerSyncing.createManager(containerId);
        this.name = this.propertyManager.addTrackedProperty(PropertyTypes.STRING);
        this.listeners = Lists.newArrayList();
        for (int i = 0; i < 4; i++) {
            listeners.add(Pair.of(
                    this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER),
                    this.propertyManager.addTrackedProperty(PropertyTypes.STRING)
            ));
        }
    }

    public ContainerLevelAccess getCallable() {
        return callable;
    }

    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (inventory.player instanceof ServerPlayer serverPlayer) {
            this.propertyManager.sendChanges(serverPlayer, false);
        }
    }

    @Override
    public void sendAllDataToRemote() {
        super.sendAllDataToRemote();
        if (inventory.player instanceof ServerPlayer serverPlayer) {
            this.propertyManager.sendChanges(serverPlayer, true);
        }
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    public List<Pair<Property<Integer>, Property<String>>> getListeners() {
        return listeners;
    }

    public Property<String> getName() {
        return name;
    }

    public abstract Class<T> getEnum();

    public T getEnumFor(int index) {
        return this.getEnum().getEnumConstants()[this.getListeners().get(index).getLeft().getOrElse(0)];
    }
}
