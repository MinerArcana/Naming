package com.minerarcana.naming.block;

import com.minerarcana.naming.blockentity.SpeakingStoneBlockEntity;
import com.minerarcana.naming.content.NamingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class SpeakingStoneBlock extends FacingMessageBlock {
    private static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public SpeakingStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean powered = pLevel.hasNeighborSignal(pPos) || pLevel.hasNeighborSignal(pPos.above());
        boolean triggered = pState.getValue(TRIGGERED);
        if (powered && !triggered) {
            pLevel.scheduleTick(pPos, this, 4);
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.TRUE), 4);
        } else if (!powered && triggered) {
            pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.FALSE), 4);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pPlacer instanceof Player) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SpeakingStoneBlockEntity speakingStoneBlockEntity) {
                speakingStoneBlockEntity.setOwner((Player) pPlacer);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRand) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof SpeakingStoneBlockEntity messageBlockEntity) {
            Component[] messages = messageBlockEntity.getMessages();
            int i = -1;
            int j = 1;

            for (int k = 0; k < messages.length; ++k) {
                if (messages[k] != Component.EMPTY && RANDOM.nextInt(j++) == 0) {
                    i = k;
                }
            }

            if (i >= 0) {
                messageBlockEntity.setHeard(messageBlockEntity.getSpeakingTarget(i)
                        .speak(messageBlockEntity.getMessage(i), messageBlockEntity)
                );
            }
        }
    }

    @Override
    public BooleanProperty getStateProperty() {
        return TRIGGERED;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(@Nonnull BlockState pState) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof SpeakingStoneBlockEntity) {
            return ((SpeakingStoneBlockEntity) blockEntity).getHeard() ? 15 : 0;
        }
        return super.getAnalogOutputSignal(pBlockState, pLevel, pPos);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return NamingBlocks.SPEAKING_STONE.getSibling(ForgeRegistries.BLOCK_ENTITIES)
                .map(type -> new SpeakingStoneBlockEntity(type, pPos, pState))
                .orElseThrow(() -> new IllegalStateException("Failed to Find Block Entity Type"));
    }
}
