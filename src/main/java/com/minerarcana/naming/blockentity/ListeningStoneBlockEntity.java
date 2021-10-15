package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.content.NamingBlocks;
import com.minerarcana.naming.mixin.SignTileEntityAccessor;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ListeningStoneBlockEntity extends PosterBoardBlockEntity {
    private Collection<String> listeningFor;

    public ListeningStoneBlockEntity(TileEntityType<PosterBoardBlockEntity> blockEntityTileEntityType) {
        super(blockEntityTileEntityType);
    }

    @Nonnull
    @Override
    public TileEntityType<?> getType() {
        return NamingBlocks.LISTENING_STONE.getSibling(ForgeRegistries.TILE_ENTITIES)
                .orElseThrow(() -> new IllegalStateException("Failed to find Listening Stone Type"));
    }

    public Collection<String> getListeningFor() {
        if (listeningFor == null) {
            listeningFor = Arrays.stream(((SignTileEntityAccessor) this).getMessages())
                    .map(ITextComponent::getContents)
                    .filter(string -> !string.isEmpty())
                    .collect(Collectors.toSet());
        }
        return listeningFor;
    }

    public boolean checkMessages() {
        if (this.getLevel() instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld) this.getLevel();
            Collection<String> messages = this.getListeningFor();
            if (!messages.isEmpty()) {
                return serverLevel.getDataStorage()
                        .computeIfAbsent(ListeningWorldData::new, "spoken")
                        .checkSpoken(messages, serverLevel);
            }
        }
        return false;
    }
}
