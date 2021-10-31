package com.minerarcana.naming.capability;

import com.google.common.collect.Sets;
import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class Namer implements INamer, INBTSerializable<CompoundNBT> {
    @CapabilityInject(INamer.class)
    public static Capability<INamer> CAP = null;

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
    public Collection<String> getAbilities() {
        return abilities;
    }

    @Override
    public boolean grantAbility(String name) {
        return abilities.add(name);
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("abilities", create(abilities));
        nbt.put("heard", create(heardHistory));
        nbt.put("spoken", create(spokenHistory));
        return nbt;
    }

    private ListNBT create(Collection<String> list) {
        ListNBT listNBT = new ListNBT();
        list.stream()
                .map(StringNBT::valueOf)
                .forEach(listNBT::add);
        return listNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        abilities.clear();
        addTo(nbt, "abilities", abilities::add);
        heardHistory.clear();
        addTo(nbt, "heard", heardHistory::add);
        spokenHistory.clear();
        addTo(nbt, "spoken", spokenHistory::add);
    }

    private void addTo(CompoundNBT nbt, String field, Consumer<String> consumer) {
        ListNBT nbtList = nbt.getList(field, Constants.NBT.TAG_STRING);
        for (int i = 0; i < nbtList.size(); i++) {
            consumer.accept(nbtList.getString(i));
        }
    }
}
