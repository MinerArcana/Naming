package com.minerarcana.naming.worlddata;

import com.google.common.collect.Queues;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Queue;
import java.util.UUID;

public class ListeningWorldData extends WorldSavedData {
    private final Queue<SpokenData> spokenQueue;

    public ListeningWorldData() {
        super("spoken");
        this.spokenQueue = Queues.newArrayDeque();
    }

    public void addSpoken(SpokenData spoken) {
        spokenQueue.add(spoken);
    }

    public void tick(IWorld world) {
        for (SpokenData checking = spokenQueue.peek(); checking != null && checking.getExpiration() <= world.dayTime();
             checking = spokenQueue.peek()) {
            spokenQueue.remove();
            this.setDirty();
        }
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        ListNBT spokenQueueNBT = nbt.getList("spokenQueue", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < spokenQueueNBT.size(); i++) {
            spokenQueue.add(SpokenData.fromNBT(spokenQueueNBT.getCompound(i)));
        }
    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        ListNBT spokenQueueNBT = new ListNBT();
        for (SpokenData current = spokenQueue.poll(); current != null; current = spokenQueue.poll()) {
            spokenQueueNBT.add(current.toNBT());
        }
        nbt.put("spokenQueue", spokenQueueNBT);
        return nbt;
    }

    public boolean checkSpoken(Collection<String> messages, ServerWorld serverLevel) {
        return spokenQueue.stream()
                .filter(spokenData -> messages.contains(spokenData.getSpoken()))
                .peek(spokenData -> {
                    UUID playerUUID = spokenData.getPlayerUUID();
                    if (playerUUID != null) {
                        boolean newHeard = spokenData.getNamer()
                                .map(namer -> namer.addHeardMessage(spokenData.getSpoken()))
                                .orElse(false);
                        if (newHeard) {
                            ServerPlayerEntity player = serverLevel.getServer()
                                    .getPlayerList()
                                    .getPlayer(playerUUID);
                            if (player != null) {
                                NamingCriteriaTriggers.HEARD.trigger(player, spokenData.getSpoken());
                            }
                        }
                    }
                })
                .findFirst()
                .isPresent();
    }
}
