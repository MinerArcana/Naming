package com.minerarcana.naming.worlddata;

import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.UUID;

public class SpokenData {
    private final LazyOptional<INamer> namer;
    private final String spoken;
    private final long expiration;
    private final UUID playerUUID;

    public SpokenData(LazyOptional<INamer> namer, String spoken, long expiration, UUID playerUUID) {
        this.namer = namer;
        this.spoken = spoken;
        this.expiration = expiration;
        this.playerUUID = playerUUID;
    }

    public LazyOptional<INamer> getNamer() {
        return namer;
    }

    public String getSpoken() {
        return spoken;
    }

    public long getExpiration() {
        return expiration;
    }

    @Nullable
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("spoken", spoken);
        nbt.putLong("expiration", expiration);
        if (playerUUID != null) {
            nbt.putUUID("playerUUID", playerUUID);
        }
        return nbt;
    }

    public static SpokenData fromNBT(CompoundNBT nbt) {
        return new SpokenData(
                LazyOptional.empty(),
                nbt.getString("spoken"),
                nbt.getLong("expiration"),
                nbt.hasUUID("playerUUID") ? nbt.getUUID("playerUUID") : null
        );
    }
}
