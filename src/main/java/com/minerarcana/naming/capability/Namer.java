package com.minerarcana.naming.capability;

import com.google.common.collect.Sets;
import com.minerarcana.naming.api.capability.INamer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.Collection;
import java.util.Set;

public class Namer implements INamer {
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
}
