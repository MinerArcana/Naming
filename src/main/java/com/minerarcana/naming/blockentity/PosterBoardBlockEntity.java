package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.content.NamingBlocks;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
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
}
