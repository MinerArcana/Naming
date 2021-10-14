package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.block.ListeningStoneBlock;
import com.minerarcana.naming.content.NamingBlocks;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.stream.Stream;

public class ListeningStoneBlockEntity extends PosterBoardBlockEntity implements ITickableTileEntity {
    private int ticksToUpdate = -1;

    public ListeningStoneBlockEntity(TileEntityType<PosterBoardBlockEntity> blockEntityTileEntityType) {
        super(blockEntityTileEntityType);
    }

    @Nonnull
    @Override
    public TileEntityType<?> getType() {
        return NamingBlocks.LISTENING_STONE.getSibling(ForgeRegistries.TILE_ENTITIES)
                .orElseThrow(() -> new IllegalStateException("Failed to find Listening Stone Type"));
    }

    @Override
    public void tick() {
        if (ticksToUpdate-- < 0) {
            if (!this.getBlockState().getValue(ListeningStoneBlock.LIT) && this.getLevel() instanceof ServerWorld && checkMessages()) {
                ServerWorld serverLevel = (ServerWorld) this.getLevel();
                serverLevel.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(ListeningStoneBlock.LIT, true));
                serverLevel.getBlockTicks().scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 20);
                ticksToUpdate = 30;
            } else {
                ticksToUpdate = 10;
            }
        }
    }

    public void setTicksToUpdate(int ticksToUpdate) {
        this.ticksToUpdate = ticksToUpdate;
    }

    public boolean checkMessages() {
        if (this.getLevel() instanceof ServerWorld && this instanceof IMessageCarrier) {
            ServerWorld serverLevel = (ServerWorld) this.getLevel();
            Collection<String> messages = ((IMessageCarrier) this).getMessages();
            if (!messages.isEmpty()) {
                Stream<String> worldSpoken = serverLevel.getDataStorage()
                        .computeIfAbsent(ListeningWorldData::new, "spoken")
                        .getCurrentSpoken();

                return worldSpoken.anyMatch(messages::contains);
            }
        }
        return false;
    }

    @Override
    public boolean renderSide(Direction direction) {
        return super.renderSide(direction) && direction != this.getBlockState().getValue(ListeningStoneBlock.FACING);
    }
}
