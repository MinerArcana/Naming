package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.block.ListeningStoneBlock;
import com.minerarcana.naming.container.ListeningStoneContainer;
import com.minerarcana.naming.content.NamingBlocks;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public class ListeningStoneBlockEntity extends TileEntity implements ISideText, INamedContainerProvider, Function<String, ListeningType> {
    private final ITextComponent[] messages = new ITextComponent[]{
            StringTextComponent.EMPTY,
            StringTextComponent.EMPTY,
            StringTextComponent.EMPTY,
            StringTextComponent.EMPTY
    };
    private final ListeningType[] listeningTypes = new ListeningType[]{
            ListeningType.NONE,
            ListeningType.NONE,
            ListeningType.NONE,
            ListeningType.NONE
    };
    private final IReorderingProcessor[] renderedMessages = new IReorderingProcessor[4];
    private String name = "";

    public ListeningStoneBlockEntity(TileEntityType<ListeningStoneBlockEntity> blockEntityType) {
        super(blockEntityType);
    }

    @Override
    public boolean renderSide(Direction side) {
        return side.getAxis() != Direction.Axis.Y && this.getBlockState().getValue(ListeningStoneBlock.FACING) != side;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (this.getLevel() instanceof ServerWorld) {
            ListeningWorldData listeningWorldData = ((ServerWorld) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, "spoken");
            listeningWorldData.removeListener(this.name, this.worldPosition);
            listeningWorldData.addListener(name, this.worldPosition, this);
        }
        this.name = name;
    }

    public ListeningType apply(String spoken) {
        ListeningType listeningType = ListeningType.NONE;
        for (int i = 0; i < 4; i++) {
            if (spoken.equalsIgnoreCase(messages[i].getContents())) {
                listeningType = listeningType.ordinal() > listeningTypes[i].ordinal() ? listeningType : listeningTypes[i];
            }
        }
        if (listeningType.isListening() && this.getLevel() != null) {
            this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState()
                    .setValue(ListeningStoneBlock.LIT, true)
            );
            this.getLevel().getBlockTicks()
                    .scheduleTick(this.getBlockPos(), NamingBlocks.LISTENING_STONE.get(), 20);
        }
        return listeningType;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setLevelAndPosition(World level, BlockPos position) {
        super.setLevelAndPosition(level, position);
        if (this.getLevel() instanceof ServerWorld) {
            ListeningWorldData listeningWorldData = ((ServerWorld) this.getLevel()).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, "spoken");
            listeningWorldData.addListener(this.name, this.worldPosition, this);
        }
    }

    public ListeningType getListeningType(int index) {
        return listeningTypes[index];
    }

    public void setListeningType(int index, ListeningType listeningType) {
        listeningTypes[index] = listeningType;
    }

    public ITextComponent getMessage(int index) {
        return messages[index];
    }

    public void setMessage(int index, ITextComponent message) {
        messages[index] = message;
        renderedMessages[index] = null;
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
    @Nonnull
    public ITextComponent getDisplayName() {
        return new StringTextComponent(this.name);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int containerId, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new ListeningStoneContainer(
                containerId,
                this
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        loadMessages(nbt);
    }

    private void loadMessages(CompoundNBT nbt) {
        this.name = nbt.getString("name");
        ListNBT messagesNBT = nbt.getList("messages", Constants.NBT.TAG_STRING);
        for (int i = 0; i < messagesNBT.size(); i++) {
            this.messages[i] = ITextComponent.Serializer.fromJson(messagesNBT.getString(i));
            this.renderedMessages[i] = null;
        }
        ListNBT listeningTypesNBT = nbt.getList("listeningTypes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < listeningTypesNBT.size(); i++) {
            this.listeningTypes[i] = ListeningType.valueOf(listeningTypesNBT.getString(i));
        }
    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT compoundNBT) {
        return saveMessages(super.save(compoundNBT));
    }

    @Nonnull
    private CompoundNBT saveMessages(@Nonnull CompoundNBT nbt) {
        nbt.putString("name", name);
        ListNBT messagesNBT = new ListNBT();
        for (ITextComponent message : messages) {
            messagesNBT.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(message)));
        }
        nbt.put("messages", messagesNBT);
        ListNBT listeningTypeNBT = new ListNBT();
        for (ListeningType listeningType : listeningTypes) {
            listeningTypeNBT.add(StringNBT.valueOf(listeningType.name()));
        }
        nbt.put("listeningTypes", listeningTypeNBT);
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
}
