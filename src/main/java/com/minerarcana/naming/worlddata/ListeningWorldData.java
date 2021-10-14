package com.minerarcana.naming.worlddata;

import com.google.common.collect.Queues;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Queue;
import java.util.stream.Stream;

public class ListeningWorldData extends WorldSavedData {
    private final Queue<Pair<String, Long>> spokenQueue;

    public ListeningWorldData() {
        super("spoken");
        this.spokenQueue = Queues.newArrayDeque();
    }

    public void addSpoken(IWorld world, String spoken) {
        spokenQueue.add(Pair.of(spoken, world.dayTime() + 20));
    }

    public void tick(IWorld world) {
        while (spokenQueue.peek() != null && spokenQueue.peek().getRight() < world.dayTime()) {
            spokenQueue.remove();
            this.setDirty();
        }
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        CompoundNBT spokenNBT = nbt.getCompound("spoken");
        for (String key : spokenNBT.getAllKeys()) {
            spokenQueue.add(Pair.of(key, spokenNBT.getLong(key)));
        }
    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        CompoundNBT spokenNBT = new CompoundNBT();
        for (Pair<String, Long> current = spokenQueue.poll(); current != null; current = spokenQueue.poll()) {
            spokenNBT.putLong(current.getKey(), current.getValue());
        }
        return nbt;
    }

    public Stream<String> getCurrentSpoken() {
        return spokenQueue.stream().map(Pair::getLeft);
    }
}
