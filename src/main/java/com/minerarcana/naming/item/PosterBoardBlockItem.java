package com.minerarcana.naming.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class PosterBoardBlockItem extends BlockItem {

    public PosterBoardBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean updateCustomBlockEntityTag(BlockPos blockPos, Level level, @Nullable Player player,
                                                 ItemStack itemStack, BlockState blockState) {
        boolean flag = updateCustomBlockEntityTag(level, player, blockPos, itemStack);
        if (!level.isClientSide && !flag && player != null) {
            BlockEntity tileEntity = level.getBlockEntity(blockPos);
            if (tileEntity instanceof SignBlockEntity) {
                player.openTextEdit((SignBlockEntity) tileEntity);
            }
        }

        return flag;
    }
}
