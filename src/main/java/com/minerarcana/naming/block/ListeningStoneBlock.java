package com.minerarcana.naming.block;

import com.minerarcana.naming.blockentity.ListeningStoneBlockEntity;
import com.minerarcana.naming.content.NamingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
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
    public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockState.getValue(LIT) && pBlockState.getValue(FACING) == pSide) {
            return 15;
        } else {
            return 0;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockState.getValue(LIT) && pBlockState.getValue(FACING).getOpposite() == pSide) {
            return 15;
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return NamingBlocks.LISTENING_STONE.getSibling(ForgeRegistries.BLOCK_ENTITIES)
                .map(type -> new ListeningStoneBlockEntity(type, pos, state))
                .orElseThrow(() -> new IllegalStateException("Failed to Find Block Entity Type"));
    }
}
