package com.minerarcana.naming.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class ClientGetter {
    public static PlayerEntity getPlayerEntity() {
        return Minecraft.getInstance().player;
    }
}
