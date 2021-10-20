package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.block.ListeningStoneBlock;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public abstract class MessageBlockEntity extends TileEntity implements ISideText, INamedContainerProvider {
    private final ITextComponent[] messages = new ITextComponent[]{
            StringTextComponent.EMPTY,
            StringTextComponent.EMPTY,
            StringTextComponent.EMPTY,
            StringTextComponent.EMPTY
    };
    private final IReorderingProcessor[] renderedMessages = new IReorderingProcessor[4];

    private String name = "";

    public MessageBlockEntity(TileEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.WHITE;
    }

    @Override
    public IReorderingProcessor getRenderedMessage(int index, Function<ITextComponent, IReorderingProcessor> generate) {
        if (renderedMessages[index] == null) {
            renderedMessages[index] = generate.apply(messages[index]);
        }
        return renderedMessages[index];
    }

    public ITextComponent getMessage(int index) {
        return messages[index];
    }

    public void setMessage(int index, ITextComponent message) {
        messages[index] = message;
        renderedMessages[index] = null;
        this.setChanged();
        if (this.getLevel() != null) {
            this.getLevel().markAndNotifyBlock(
                    this.getBlockPos(),
                    this.getLevel().getChunkAt(this.getBlockPos()),
                    this.getBlockState(),
                    this.getBlockState(),
                    Constants.BlockFlags.DEFAULT,
                    512
            );
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        loadMessages(nbt);
    }

    protected void loadMessages(CompoundNBT nbt) {
        this.name = nbt.getString("name");
        ListNBT messagesNBT = nbt.getList("messages", Constants.NBT.TAG_STRING);
        for (int i = 0; i < messagesNBT.size(); i++) {
            this.messages[i] = ITextComponent.Serializer.fromJson(messagesNBT.getString(i));
            this.renderedMessages[i] = null;
        }
    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT compoundNBT) {
        return saveMessages(super.save(compoundNBT));
    }

    @Nonnull
    protected CompoundNBT saveMessages(@Nonnull CompoundNBT nbt) {
        nbt.putString("name", name);
        ListNBT messagesNBT = new ListNBT();
        for (ITextComponent message : messages) {
            messagesNBT.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(message)));
        }
        nbt.put("messages", messagesNBT);
        return nbt;
    }


    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.loadMessages(pkt.getTag());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, saveMessages(new CompoundNBT()));
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.loadMessages(tag);
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new StringTextComponent(this.getName());
    }

    public ITextComponent[] getMessages() {
        return messages;
    }

    @Override
    public boolean renderSide(Direction side) {
        return side.getAxis() != Direction.Axis.Y && this.getBlockState().getValue(ListeningStoneBlock.FACING) != side;
    }
}
