package com.minerarcana.naming.block;

import com.minerarcana.naming.blockentity.ListeningStoneBlockEntity;
import com.minerarcana.naming.content.NamingBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class ListeningStoneBlock extends Block {
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
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        TileEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof ListeningStoneBlockEntity) {
            if (pPlayer instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) pPlayer, (ListeningStoneBlockEntity) blockEntity);
            }
            return ActionResultType.sidedSuccess(pLevel.isClientSide());
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, LIT);
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

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand) {
        pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, false));
    }
}
