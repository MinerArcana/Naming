package com.minerarcana.naming.worlddata;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.minerarcana.naming.blockentity.ListeningType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListeningWorldData extends WorldSavedData {
    private static final Pattern NAME_CHECK = Pattern.compile("^(?<name>\\w+)(\\s+)(?<speech>.*)");
    public static final String NAME = "listening";

    private final Table<String, BlockPos, WeakFunction<String, ListeningType>> listeners;

    public ListeningWorldData() {
        super(NAME);
        this.listeners = HashBasedTable.create();
    }

    public void clean() {
        listeners.cellSet().removeIf(cell -> cell.getValue() == null || !cell.getValue().isValid());
    }

    public ListeningType speakTo(String text) {
        Matcher spokenMatch = NAME_CHECK.matcher(text);
        if (spokenMatch.find()) {
            String name = spokenMatch.group("name");
            String speech = spokenMatch.group("speech");
            return speakTo(name, speech);
        }
        return ListeningType.NONE;
    }

    public ListeningType speakTo(String speaker, String spoken) {
        Map<BlockPos, WeakFunction<String, ListeningType>> positionalListeners = listeners.row(speaker.toLowerCase(Locale.ROOT));
        if (!positionalListeners.isEmpty()) {
            return positionalListeners.values()
                    .stream()
                    .map(listener -> listener.apply(spoken))
                    .reduce((listeningType, listeningType2) ->
                            listeningType.ordinal() > listeningType2.ordinal() ? listeningType : listeningType2
                    )
                    .orElse(ListeningType.NONE);
        }
        return ListeningType.NONE;
    }

    public void removeListener(String name, BlockPos worldPosition) {
        listeners.remove(name.toLowerCase(Locale.ROOT), worldPosition);
    }

    public void addListener(String name, BlockPos worldPosition, Function<String, ListeningType> listener) {
        listeners.put(name.toLowerCase(Locale.ROOT), worldPosition.immutable(), new WeakFunction<>(listener, ListeningType.NONE));
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {

    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        return nbt;
    }
}
