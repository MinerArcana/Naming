package com.minerarcana.naming.block;

import com.minerarcana.naming.blockentity.PosterBoardBlockEntity;
import com.minerarcana.naming.mixin.SignTileEntityAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class SpeakingStoneBlock extends PosterBoardBlock {
    public static BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public SpeakingStoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(TRIGGERED, false)
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, TRIGGERED);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return state.getValue(FACING).getOpposite() == side;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean powered = pLevel.hasNeighborSignal(pPos) || pLevel.hasNeighborSignal(pPos.above());
        boolean triggered = pState.getValue(TRIGGERED);
        if (powered && !triggered) {
            pLevel.getBlockTicks().scheduleTick(pPos, this, 4);
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.TRUE), 4);
        } else if (!powered && triggered) {
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.FALSE), 4);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand) {
        TileEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof PosterBoardBlockEntity) {
            PosterBoardBlockEntity posterBoardBlockEntity = (PosterBoardBlockEntity) blockEntity;
            ITextComponent[] messages = ((SignTileEntityAccessor) posterBoardBlockEntity).getMessages();
            int i = -1;
            int j = 1;

            for (int k = 0; k < messages.length; ++k) {
                if (messages[k] != ITextComponent.EMPTY && RANDOM.nextInt(j++) == 0) {
                    i = k;
                }
            }

            if (i >= 0) {
                //TODO: Speak?
            }
        }
    }
}
