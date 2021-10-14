package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.content.NamingBlocks;
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

    public boolean renderSide(Direction direction){
        return direction.getAxis() != Direction.Axis.Y;
    }
}
