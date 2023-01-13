package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.block.ListeningStoneBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public abstract class MessageBlockEntity extends BlockEntity implements ISideText, MenuProvider {
    private final Component[] messages = new Component[]{
            Component.empty(),
            Component.empty(),
            Component.empty(),
            Component.empty()
    };
    private final FormattedCharSequence[] renderedMessages = new FormattedCharSequence[4];

    private String name = "";

    public MessageBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    @Override
    public DyeColor getTextColor() {
        return DyeColor.WHITE;
    }

    @Override
    public FormattedCharSequence getRenderedMessage(int index, Function<Component, FormattedCharSequence> generate) {
        if (renderedMessages[index] == null) {
            renderedMessages[index] = generate.apply(messages[index]);
        }
        return renderedMessages[index];
    }

    public Component getMessage(int index) {
        return messages[index];
    }

    public void setMessage(int index, Component message) {
        messages[index] = message;
        renderedMessages[index] = null;
        this.setChanged();
        if (this.getLevel() != null) {
            this.getLevel().markAndNotifyBlock(
                    this.getBlockPos(),
                    this.getLevel().getChunkAt(this.getBlockPos()),
                    this.getBlockState(),
                    this.getBlockState(),
                    Block.UPDATE_ALL,
                    512
            );
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void load(CompoundTag nbt) {
        super.load(nbt);
        loadMessages(nbt);
    }

    protected void loadMessages(CompoundTag nbt) {
        this.name = nbt.getString("name");
        ListTag messagesNBT = nbt.getList("messages", Tag.TAG_STRING);
        for (int i = 0; i < messagesNBT.size(); i++) {
            this.messages[i] = Component.Serializer.fromJson(messagesNBT.getString(i));
            this.renderedMessages[i] = null;
        }
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compoundNBT) {
        super.saveAdditional(compoundNBT);
        saveMessages(compoundNBT);
    }

    @Nonnull
    protected CompoundTag saveMessages(@Nonnull CompoundTag nbt) {
        nbt.putString("name", name);
        ListTag messagesNBT = new ListTag();
        for (Component message : messages) {
            messagesNBT.add(StringTag.valueOf(Component.Serializer.toJson(message)));
        }
        nbt.put("messages", messagesNBT);
        return nbt;
    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) {
            this.loadMessages(pkt.getTag());
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.loadMessages(tag);
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        saveAdditional(compoundTag);
        return compoundTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return Component.literal(this.getName());
    }

    public Component[] getMessages() {
        return messages;
    }

    @Override
    public boolean renderSide(Direction side) {
        return side.getAxis() != Direction.Axis.Y && this.getBlockState().getValue(ListeningStoneBlock.FACING) != side;
    }
}
