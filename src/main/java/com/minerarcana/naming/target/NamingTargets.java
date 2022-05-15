package com.minerarcana.naming.target;

import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;
import java.util.Map;
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

    public <T extends INamingTarget> NamingTargetType register(String name, Function<FriendlyByteBuf, T> creator) {
        NamingTargetType targetType = new NamingTargetType(name, creator);
        this.targetTypes.put(name, targetType);
        return targetType;
    }

    public void toPacketBuffer(INamingTarget namingTarget, FriendlyByteBuf packetBuffer) {
        NamingTargetType targetType = namingTarget.getType();
        packetBuffer.writeUtf(targetType.name());
        namingTarget.toPacketBuffer(packetBuffer);
    }

    @Nullable
    public INamingTarget fromPacketBuffer(FriendlyByteBuf packetBuffer) {
        String name = packetBuffer.readUtf();
        NamingTargetType targetType = targetTypes.get(name);
        if (targetType != null) {
            return targetType.creator().apply(packetBuffer);
        } else {
            return null;
        }
    }
}
