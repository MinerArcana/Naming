package com.minerarcana.naming.network.property;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import SimpleChannel;

public class PropertyNetwork {
    private static final String VERSION = "1";
    private final SimpleChannel channel;

    public PropertyNetwork(String id) {
        this.channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(id, "properties"),
                () -> VERSION,
                VERSION::equals,
                VERSION::equals
        );

        this.channel.messageBuilder(UpdateClientContainerPropertiesMessage.class, 0)
                .decoder(UpdateClientContainerPropertiesMessage::decode)
                .encoder(UpdateClientContainerPropertiesMessage::encode)
                .consumer(UpdateClientContainerPropertiesMessage::consume)
                .add();

        this.channel.messageBuilder(UpdateServerContainerPropertyMessage.class, 1)
                .decoder(UpdateServerContainerPropertyMessage::decode)
                .encoder(UpdateServerContainerPropertyMessage::encode)
                .consumer(UpdateServerContainerPropertyMessage::consume)
                .add();
    }

    public void sendServerUpdate(UpdateServerContainerPropertyMessage message) {
        this.channel.send(PacketDistributor.SERVER.noArg(), message);
    }

    public void sendClientUpdate(ServerPlayerEntity playerEntity, UpdateClientContainerPropertiesMessage message) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> playerEntity), message);
    }
}
