package com.minerarcana.naming.worlddata;

import com.google.common.collect.Queues;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;
import java.util.Queue;

public class EchoingWorldData extends SavedData {
    public static final String NAME = "echoing";

    private final Queue<EchoingData> echoingDataQueue;

    public EchoingWorldData() {
        echoingDataQueue = Queues.newArrayDeque();
    }

    public EchoingWorldData(CompoundTag tag) {
        echoingDataQueue = Queues.newArrayDeque();
    }

    public void tick(LevelAccessor world) {
        while (!echoingDataQueue.isEmpty() && echoingDataQueue.peek().getEchoTime() <= world.dayTime()) {
            EchoingData newEchoingData = echoingDataQueue.remove().doEcho(world);
            if (newEchoingData != null) {
                echoingDataQueue.add(newEchoingData);
            }
        }
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag pCompound) {
        return pCompound;
    }
}
