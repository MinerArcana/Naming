package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.content.NamingBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class PosterBoardBlockEntity extends SignTileEntity {
    @SuppressWarnings("Unused")
    public PosterBoardBlockEntity(TileEntityType<PosterBoardBlockEntity> blockEntityTileEntityType) {
        super();
    }

    @Nonnull
    public TileEntityType<?> getType() {
        return NamingBlocks.POSTER_BOARD.getSibling(ForgeRegistries.TILE_ENTITIES)
                .orElseThrow(() -> new IllegalStateException("No Poster Board Block Entity Type Found"));
    }

    public boolean renderSide(Direction direction) {
        if (direction.getAxis() != Direction.Axis.Y) {
            BlockState blockState = this.getBlockState();
            if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                return direction != blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
