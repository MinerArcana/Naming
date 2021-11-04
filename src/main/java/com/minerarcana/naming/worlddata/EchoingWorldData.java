package com.minerarcana.naming.worlddata;

import com.google.common.collect.Queues;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.Queue;

public class EchoingWorldData extends WorldSavedData {
    public static final String NAME = "echoing";

    private final Queue<EchoingData> echoingDataQueue;

    public EchoingWorldData() {
        super(NAME);
        echoingDataQueue = Queues.newArrayDeque();
    }

    public void tick(IWorld world) {
        while (!echoingDataQueue.isEmpty() && echoingDataQueue.peek().getEchoTime() <= world.dayTime()) {
            EchoingData newEchoingData = echoingDataQueue.remove().doEcho(world);
            if (newEchoingData != null) {
                echoingDataQueue.add(newEchoingData);
            }
        }
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {

    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT pCompound) {
        return pCompound;
    }
}
