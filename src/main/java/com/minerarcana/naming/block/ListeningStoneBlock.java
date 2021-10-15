package com.minerarcana.naming.block;

import com.minerarcana.naming.blockentity.ListeningStoneBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class ListeningStoneBlock extends PosterBoardBlock {
    public static BooleanProperty LIT = BlockStateProperties.LIT;
    public static EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ListeningStoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(LIT, false)
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, LIT);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return state.getValue(FACING) == side;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (!pLevel.isClientSide()) {
            pLevel.getBlockTicks()
                    .scheduleTick(pPos, this, 20);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand) {
        TileEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof ListeningStoneBlockEntity) {
            ListeningStoneBlockEntity listeningStoneBlockEntity = (ListeningStoneBlockEntity) blockEntity;
            if (listeningStoneBlockEntity.checkMessages()) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, true));
            } else {
                if (pState.getValue(LIT)) {
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, false));
                }
            }
            pLevel.getBlockTicks()
                    .scheduleTick(pPos, this, 20);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getDirectSignal(BlockState pBlockState, IBlockReader pBlockAccess, BlockPos pPos, Direction pSide) {
        if (pBlockState.getValue(LIT) && pBlockState.getValue(FACING).getOpposite() == pSide) {
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
        return new ListeningStoneBlockEntity(null);
    }
}
