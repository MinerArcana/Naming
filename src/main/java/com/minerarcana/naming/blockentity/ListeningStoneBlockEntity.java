package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.block.ListeningStoneBlock;
import com.minerarcana.naming.container.ListeningContainer;
import com.minerarcana.naming.content.NamingBlocks;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public class ListeningStoneBlockEntity extends MessageBlockEntity implements Function<String, ListeningType> {
    private final ListeningType[] listeningTypes = {
            ListeningType.NONE,
            ListeningType.NONE,
            ListeningType.NONE,
            ListeningType.NONE
    };

    public ListeningStoneBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    @Override
    public void setName(String name) {
        if (this.getLevel() instanceof ServerLevel) {
            ListeningWorldData listeningWorldData = ((ServerLevel) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData::new, ListeningWorldData.NAME);
            listeningWorldData.removeListener(this.getName(), this.getBlockPos());
            listeningWorldData.addListener(name, this.getBlockPos(), this);
        }
        super.setName(name);
    }

    public ListeningType apply(String spoken) {
        ListeningType listeningType = ListeningType.NONE;
        for (int i = 0; i < 4; i++) {
            if (spoken.equalsIgnoreCase(this.getMessage(i).getContents())) {
                listeningType = listeningType.ordinal() > listeningTypes[i].ordinal() ? listeningType : listeningTypes[i];
            }
        }
        if (listeningType.isListening() && this.getLevel() != null) {
            this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState()
                    .setValue(ListeningStoneBlock.LIT, true)
            );
            this.getLevel().scheduleTick(this.getBlockPos(), NamingBlocks.LISTENING_STONE.get(), 20);
        }
        return listeningType;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setLevel(Level level) {
        super.setLevel(level);
        if (this.getLevel() instanceof ServerLevel) {
            ((ServerLevel) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData::new, ListeningWorldData.NAME)
                    .addListener(this.getName(), this.worldPosition, this);
        }
    }

    public ListeningType getListeningType(int index) {
        return listeningTypes[index];
    }

    public void setListeningType(int index, ListeningType listeningType) {
        listeningTypes[index] = listeningType;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player playerEntity) {
        return new ListeningContainer(
                containerId,
                inventory,
                this
        );
    }

    @Override
    protected void loadMessages(CompoundTag nbt) {
        super.loadMessages(nbt);
        ListTag listeningTypesNBT = nbt.getList("listeningTypes", Tag.TAG_STRING);
        for (int i = 0; i < listeningTypesNBT.size(); i++) {
            this.listeningTypes[i] = ListeningType.valueOf(listeningTypesNBT.getString(i));
        }
    }

    @Nonnull
    protected CompoundTag saveMessages(@Nonnull CompoundTag nbt) {
        nbt = super.saveMessages(nbt);
        ListTag listeningTypeNBT = new ListTag();
        for (ListeningType listeningType : listeningTypes) {
            listeningTypeNBT.add(StringTag.valueOf(listeningType.name()));
        }
        nbt.put("listeningTypes", listeningTypeNBT);
        return nbt;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.getLevel() instanceof ServerLevel) {
            ListeningWorldData listeningWorldData = ((ServerLevel) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData::new, ListeningWorldData.NAME);
            listeningWorldData.removeListener(this.getName(), this.getBlockPos());
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (this.getLevel() instanceof ServerLevel) {
            ListeningWorldData listeningWorldData = ((ServerLevel) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData::new, ListeningWorldData.NAME);
            listeningWorldData.removeListener(this.getName(), this.getBlockPos());
        }
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        if (this.getLevel() instanceof ServerLevel) {
            ListeningWorldData listeningWorldData = ((ServerLevel) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData::new, ListeningWorldData.NAME);
            listeningWorldData.addListener(this.getName(), this.getBlockPos(), this);
        }
    }
}
