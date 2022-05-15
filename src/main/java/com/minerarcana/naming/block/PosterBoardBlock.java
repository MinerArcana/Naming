package com.minerarcana.naming.block;

import com.minerarcana.naming.blockentity.PosterBoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class PosterBoardBlock extends Block implements EntityBlock {
    public PosterBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState pState, Level level, BlockPos pPos, Player player, InteractionHand hand, BlockHitResult pHit) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean canColor = itemstack.getItem() instanceof DyeItem && player.getAbilities().mayBuild;
        if (level.isClientSide) {
            return canColor ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pPos);
            if (blockEntity instanceof PosterBoardBlockEntity posterBoardBlockEntity) {
                if (canColor) {
                    boolean colorSet = posterBoardBlockEntity.setColor(((DyeItem) itemstack.getItem()).getDyeColor());
                    if (colorSet && !player.isCreative()) {
                        itemstack.shrink(1);
                    }
                }

                return player instanceof ServerPlayer && posterBoardBlockEntity.executeClickCommands((ServerPlayer) player) ?
                        InteractionResult.SUCCESS : InteractionResult.PASS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PosterBoardBlockEntity(null, pPos, pState);
    }
}
