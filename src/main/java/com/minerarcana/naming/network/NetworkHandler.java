package com.minerarcana.naming.network;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.spell.Spell;
import com.minerarcana.naming.target.INamingTarget;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;

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

        this.channel.messageBuilder(SpellClientMessage.class, 2)
                .decoder(SpellClientMessage::decode)
                .encoder(SpellClientMessage::encode)
                .consumer(SpellClientMessage::consume)
                .add();

        this.channel.messageBuilder(SyncNamingMessage.class, 3)
                .decoder(SyncNamingMessage::decode)
                .encoder(SyncNamingMessage::encode)
                .consumer(SyncNamingMessage::consume)
                .add();
    }

    public void name(@Nullable String name, INamingTarget target) {
        this.channel.send(PacketDistributor.SERVER.noArg(), new NameTargetMessage(target, name));
    }

    public void spellToServer(Spell spell, String spoken, int[] targeted) {
        this.channel.send(PacketDistributor.SERVER.noArg(), new SpellServerMessage(spell, spoken, targeted));
    }

    public void spellToClient(ServerPlayer player, Spell spell, String spoken) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> player), new SpellClientMessage(spell, spoken));
    }

    public void syncCap(ServerPlayer player, SyncNamingMessage message) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
