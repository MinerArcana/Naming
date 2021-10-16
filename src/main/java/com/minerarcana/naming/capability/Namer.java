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

public class Namer implements INamer, INBTSerializable<CompoundNBT> {
    @CapabilityInject(INamer.class)
    public static Capability<INamer> CAP = null;

    private final Set<String> abilities;
    private final Set<String> heard;

    public Namer() {
        this(Sets.newHashSet(), Sets.newHashSet());
    }


    public Namer(Set<String> abilities, Set<String> heard) {
        this.abilities = abilities;
        this.heard = heard;
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
    public boolean addHeardMessage(String spoken) {
        return heard.add(spoken);
    }

    @Override
    public Collection<String> getHeardMessages() {
        return heard;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT abilitiesNBT = new ListNBT();
        abilities.forEach(ability -> abilitiesNBT.add(StringNBT.valueOf(ability)));
        nbt.put("abilities", abilitiesNBT);
        ListNBT heardNBT = new ListNBT();
        heard.forEach(value -> heardNBT.add(StringNBT.valueOf(value)));
        nbt.put("heard", heardNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT abilitiesNBT = nbt.getList("abilities", Constants.NBT.TAG_STRING);
        abilities.clear();
        for (int i = 0; i < abilitiesNBT.size(); i++) {
            abilities.add(abilitiesNBT.getString(i));
        }
        ListNBT heardNBT = nbt.getList("heard", Constants.NBT.TAG_STRING);
        heard.clear();
        for (int i = 0; i < heardNBT.size(); i++) {
            heard.add(heardNBT.getString(i));
        }
    }
}
