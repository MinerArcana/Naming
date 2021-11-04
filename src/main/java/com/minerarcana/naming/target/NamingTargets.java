package com.minerarcana.naming.target;

import com.google.common.collect.Maps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NamingTargets {
    public static final NamingTargets INSTANCE = new NamingTargets();

    public static final NamingTargetType ENTITY = INSTANCE.register(
            "entity",
            EntityNamingTarget::fromPacketBuffer
    );

    public static final NamingTargetType ITEMSTACK = INSTANCE.register(
            "itemstack",
            ItemStackNamingTarget::fromPacketBuffer
    );

    public static final NamingTargetType EMPTY = INSTANCE.register(
            "empty",
            packetBuffer -> new EmptyTarget()
    );

    private final Map<String, NamingTargetType> targetTypes;

    public NamingTargets() {
        targetTypes = Maps.newHashMap();
    }

    public <T extends INamingTarget> NamingTargetType register(String name, Function<PacketBuffer, T> creator) {
        NamingTargetType targetType = new NamingTargetType(name, creator);
        this.targetTypes.put(name, targetType);
        return targetType;
    }

    public void toPacketBuffer(INamingTarget namingTarget, PacketBuffer packetBuffer) {
        NamingTargetType targetType = namingTarget.getType();
        packetBuffer.writeUtf(targetType.getName());
        namingTarget.toPacketBuffer(packetBuffer);
    }

    @Nullable
    public INamingTarget fromPacketBuffer(PacketBuffer packetBuffer) {
        String name = packetBuffer.readUtf(32767);
        NamingTargetType targetType = targetTypes.get(name);
        if (targetType != null) {
            return targetType.getCreator().apply(packetBuffer);
        } else {
            return null;
        }
    }
}
