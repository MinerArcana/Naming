package com.minerarcana.naming.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientGetter {
    public static Player getPlayerEntity() {
        return Minecraft.getInstance().player;
    }
}
