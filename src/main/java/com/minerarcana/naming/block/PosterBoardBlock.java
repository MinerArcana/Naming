package com.minerarcana.naming.block;

import com.minerarcana.naming.blockentity.PosterBoardBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class PosterBoardBlock extends Block {
    public PosterBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType use(BlockState blockState, World level, BlockPos blockPos, PlayerEntity player, Hand hand,
                                BlockRayTraceResult rayTraceResult) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean canColor = itemstack.getItem() instanceof DyeItem && player.abilities.mayBuild;
        if (level.isClientSide) {
            return canColor ? ActionResultType.SUCCESS : ActionResultType.CONSUME;
        } else {
            TileEntity tileentity = level.getBlockEntity(blockPos);
            if (tileentity instanceof PosterBoardBlockEntity) {
                PosterBoardBlockEntity posterBoardBlockEntity = (PosterBoardBlockEntity) tileentity;
                if (canColor) {
                    boolean colorSet = posterBoardBlockEntity.setColor(((DyeItem) itemstack.getItem()).getDyeColor());
                    if (colorSet && !player.isCreative()) {
                        itemstack.shrink(1);
                    }
                }

                return posterBoardBlockEntity.executeClickCommands(player) ? ActionResultType.SUCCESS : ActionResultType.PASS;
            } else {
                return ActionResultType.PASS;
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PosterBoardBlockEntity(null);
    }
}
