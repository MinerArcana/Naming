package com.minerarcana.naming.block;

import com.minerarcana.naming.content.NamingBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ListeningStoneBlock extends FacingMessageBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public ListeningStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BooleanProperty getStateProperty() {
        return LIT;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getDirectSignal(BlockState pBlockState, IBlockReader pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockState.getValue(LIT) && pBlockState.getValue(FACING) == pSide) {
            return 15;
        } else {
            return 0;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getSignal(BlockState pBlockState, IBlockReader pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockState.getValue(LIT) && pBlockState.getValue(FACING).getOpposite() == pSide) {
            return 15;
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return NamingBlocks.LISTENING_STONE.getSibling(ForgeRegistries.TILE_ENTITIES)
                .map(TileEntityType::create)
                .orElseThrow(() -> new IllegalStateException("Failed to Find Block Entity Type"));
    }
}
