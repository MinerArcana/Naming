package com.minerarcana.naming.block;

import com.minerarcana.naming.blockentity.SpeakingStoneBlockEntity;
import com.minerarcana.naming.content.NamingBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
    @ParametersAreNonnullByDefault
    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pPlacer instanceof PlayerEntity) {
            TileEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SpeakingStoneBlockEntity) {
                ((SpeakingStoneBlockEntity) blockEntity).setOwner((PlayerEntity) pPlacer);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand) {
        TileEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof SpeakingStoneBlockEntity) {
            SpeakingStoneBlockEntity messageBlockEntity = (SpeakingStoneBlockEntity) blockEntity;
            ITextComponent[] messages = messageBlockEntity.getMessages();
            int i = -1;
            int j = 1;

            for (int k = 0; k < messages.length; ++k) {
                if (messages[k] != ITextComponent.EMPTY && RANDOM.nextInt(j++) == 0) {
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
    public int getAnalogOutputSignal(BlockState pBlockState, World pLevel, BlockPos pPos) {
        TileEntity tileEntity = pLevel.getBlockEntity(pPos);
        if (tileEntity instanceof SpeakingStoneBlockEntity) {
            return ((SpeakingStoneBlockEntity) tileEntity).getHeard() ? 15 : 0;
        }
        return super.getAnalogOutputSignal(pBlockState, pLevel, pPos);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return NamingBlocks.SPEAKING_STONE.getSibling(ForgeRegistries.TILE_ENTITIES)
                .map(TileEntityType::create)
                .orElseThrow(() -> new IllegalStateException("Failed to Find Block Entity Type"));
    }
}
