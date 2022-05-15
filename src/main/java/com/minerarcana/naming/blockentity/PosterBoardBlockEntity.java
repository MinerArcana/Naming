package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.content.NamingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class PosterBoardBlockEntity extends SignBlockEntity implements ISideText {
    @SuppressWarnings("unused")
    public PosterBoardBlockEntity(BlockEntityType<?> ignoredBlockEntityType, BlockPos pPos, BlockState pState) {
        super(pPos, pState);
    }

    @Nonnull
    public BlockEntityType<?> getType() {
        return NamingBlocks.POSTER_BOARD.getSibling(ForgeRegistries.BLOCK_ENTITIES)
                .orElseThrow(() -> new IllegalStateException("No Poster Board Block Entity Type Found"));
    }

    @Override
    public boolean renderSide(Direction direction) {
        if (direction.getAxis() != Direction.Axis.Y) {
            BlockState blockState = this.getBlockState();
            if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                return direction != blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public DyeColor getTextColor() {
        return this.getColor();
    }

    @Override
    public FormattedCharSequence getRenderedMessage(int index, Function<Component, FormattedCharSequence> generate) {
        return this.getRenderMessages(false, generate)[index];
    }
}
