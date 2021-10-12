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

    public Namer() {
        this(Sets.newHashSet());
    }


    public Namer(Set<String> abilities) {
        this.abilities = abilities;
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT abilitiesNBT = new ListNBT();
        abilities.forEach(ability -> abilitiesNBT.add(StringNBT.valueOf(ability)));
        nbt.put("abilities", abilitiesNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT abilitiesNBT = nbt.getList("abilities", Constants.NBT.TAG_STRING);
        abilities.clear();
        for (int i = 0; i < abilitiesNBT.size(); i++) {
            abilities.add(abilitiesNBT.getString(i));
        }
    }
}
