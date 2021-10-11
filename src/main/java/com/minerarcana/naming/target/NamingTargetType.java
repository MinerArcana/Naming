package com.minerarcana.naming.target;

import net.minecraft.network.PacketBuffer;

import java.util.function.Function;

public class NamingTargetType {
    private final String name;
    private final Function<PacketBuffer, ? extends INamingTarget> creator;

    public NamingTargetType(String name, Function<PacketBuffer, ? extends INamingTarget> creator) {
        this.name = name;
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public Function<PacketBuffer, ? extends INamingTarget> getCreator() {
        return creator;
    }
}
