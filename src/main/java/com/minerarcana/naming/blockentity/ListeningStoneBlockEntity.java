package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.block.ListeningStoneBlock;
import com.minerarcana.naming.container.ListeningContainer;
import com.minerarcana.naming.content.NamingBlocks;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

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

    public ListeningStoneBlockEntity(TileEntityType<ListeningStoneBlockEntity> blockEntityType) {
        super(blockEntityType);
    }

    @Override
    public void setName(String name) {
        if (this.getLevel() instanceof ServerWorld) {
            ListeningWorldData listeningWorldData = ((ServerWorld) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData.NAME);
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
            this.getLevel().getBlockTicks()
                    .scheduleTick(this.getBlockPos(), NamingBlocks.LISTENING_STONE.get(), 20);
        }
        return listeningType;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setLevelAndPosition(World level, BlockPos position) {
        super.setLevelAndPosition(level, position);
        if (this.getLevel() instanceof ServerWorld) {
            ((ServerWorld) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData.NAME)
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
    public Container createMenu(int containerId, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new ListeningContainer(
                containerId,
                this
        );
    }

    @Override
    protected void loadMessages(CompoundNBT nbt) {
        super.loadMessages(nbt);
        ListNBT listeningTypesNBT = nbt.getList("listeningTypes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < listeningTypesNBT.size(); i++) {
            this.listeningTypes[i] = ListeningType.valueOf(listeningTypesNBT.getString(i));
        }
    }

    @Nonnull
    protected CompoundNBT saveMessages(@Nonnull CompoundNBT nbt) {
        nbt = super.saveMessages(nbt);
        ListNBT listeningTypeNBT = new ListNBT();
        for (ListeningType listeningType : listeningTypes) {
            listeningTypeNBT.add(StringNBT.valueOf(listeningType.name()));
        }
        nbt.put("listeningTypes", listeningTypeNBT);
        return nbt;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.getLevel() instanceof ServerWorld) {
            ListeningWorldData listeningWorldData = ((ServerWorld) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData.NAME);
            listeningWorldData.removeListener(this.getName(), this.getBlockPos());
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (this.getLevel() instanceof ServerWorld) {
            ListeningWorldData listeningWorldData = ((ServerWorld) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData.NAME);
            listeningWorldData.removeListener(this.getName(), this.getBlockPos());
        }
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        if (this.getLevel() instanceof ServerWorld) {
            ListeningWorldData listeningWorldData = ((ServerWorld) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, ListeningWorldData.NAME);
            listeningWorldData.addListener(this.getName(), this.getBlockPos(), this);
        }
    }
}
