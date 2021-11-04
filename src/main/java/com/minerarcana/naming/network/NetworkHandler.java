package com.minerarcana.naming.network;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.spell.Spell;
import com.minerarcana.naming.target.INamingTarget;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    private final SimpleChannel channel;

    public NetworkHandler() {
        this(ChannelBuilder.named(Naming.rl("network"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(NetworkRegistry.acceptMissingOr("1"))
                .serverAcceptedVersions(NetworkRegistry.acceptMissingOr("1"))
                .simpleChannel()
        );
    }

    public NetworkHandler(SimpleChannel channel) {
        this.channel = channel;
        this.channel.messageBuilder(NameTargetMessage.class, 0)
                .decoder(NameTargetMessage::decode)
                .encoder(NameTargetMessage::encode)
                .consumer(NameTargetMessage::consume)
                .add();
        this.channel.messageBuilder(SpellServerMessage.class, 1)
                .decoder(SpellServerMessage::decode)
                .encoder(SpellServerMessage::encode)
                .consumer(SpellServerMessage::consume)
                .add();

        this.channel.messageBuilder(SpellClientMessage.class, 1)
                .decoder(SpellClientMessage::decode)
                .encoder(SpellClientMessage::encode)
                .consumer(SpellClientMessage::consume)
                .add();
    }

    public void name(String name, INamingTarget target) {
        this.channel.send(PacketDistributor.SERVER.noArg(), new NameTargetMessage(target, name));
    }

    public void spellToServer(Spell spell, String spoken, int[] targeted) {
        this.channel.send(PacketDistributor.SERVER.noArg(), new SpellServerMessage(spell, spoken, targeted));
    }

    public void spellToClient(ServerPlayerEntity player, Spell spell, String spoken) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> player), new SpellClientMessage(spell, spoken));
    }
}
