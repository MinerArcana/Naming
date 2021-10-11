package com.minerarcana.naming.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class PosterBoardBlockItem extends BlockItem {

    public PosterBoardBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean updateCustomBlockEntityTag(BlockPos blockPos, World level, @Nullable PlayerEntity player,
                                                 ItemStack itemStack, BlockState blockState) {
        boolean flag = updateCustomBlockEntityTag(level, player, blockPos, itemStack);
        if (!level.isClientSide && !flag && player != null) {
            TileEntity tileEntity = level.getBlockEntity(blockPos);
            if (tileEntity instanceof SignTileEntity) {
                player.openTextEdit((SignTileEntity) tileEntity);
            }
        }

        return flag;
    }
}
