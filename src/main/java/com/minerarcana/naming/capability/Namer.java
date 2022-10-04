package com.minerarcana.naming.capability;

import com.google.common.collect.Sets;
import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class Namer implements INamer, INBTSerializable<CompoundTag> {
    public static Capability<INamer> CAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    private final Set<String> abilities;
    private final Set<String> heardHistory;
    private final Set<String> spokenHistory;

    public Namer() {
        this(Sets.newHashSet(), Sets.newHashSet(), Sets.newHashSet());
    }

    public Namer(Set<String> abilities, Set<String> heard, Set<String> spoken) {
        this.abilities = abilities;
        this.heardHistory = heard;
        this.spokenHistory = spoken;
    }

    @Override
    @Nonnull
    public Collection<String> getAbilities() {
        return abilities;
    }

    @Override
    public boolean grantAbility(String name) {
        return abilities.add(name);
    }

    @Override
    public boolean removeAbility(String name) {
        return abilities.remove(name);
    }

    @Override
    public boolean hasAbility(String name) {
        return abilities.contains(name);
    }

    @Override
    public void speakTo(String phrase) {
        heardHistory.add(phrase);
    }

    @Override
    public Collection<String> getHeardHistory() {
        return heardHistory;
    }

    @Override
    public void heardBy(String phrase) {
        spokenHistory.add(phrase);
    }

    @Override
    public Collection<String> getSpokenHistory() {
        return spokenHistory;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("abilities", create(abilities));
        nbt.put("heard", create(heardHistory));
        nbt.put("spoken", create(spokenHistory));
        return nbt;
    }

    private ListTag create(Collection<String> list) {
        ListTag listNBT = new ListTag();
        list.stream()
                .map(StringTag::valueOf)
                .forEach(listNBT::add);
        return listNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        abilities.clear();
        addTo(nbt, "abilities", abilities::add);
        heardHistory.clear();
        addTo(nbt, "heard", heardHistory::add);
        spokenHistory.clear();
        addTo(nbt, "spoken", spokenHistory::add);
    }

    private void addTo(CompoundTag nbt, String field, Consumer<String> consumer) {
        ListTag nbtList = nbt.getList(field, Tag.TAG_STRING);
        for (int i = 0; i < nbtList.size(); i++) {
            consumer.accept(nbtList.getString(i));
        }
    }

    public void setAbilities(Collection<String> abilities) {
        this.abilities.clear();
        this.abilities.addAll(abilities);
    }
}
