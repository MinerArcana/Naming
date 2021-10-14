package com.minerarcana.naming.event;


import com.minerarcana.naming.Naming;
import com.minerarcana.naming.capability.NamingCapabilityProvider;
import com.minerarcana.naming.command.NamingCommand;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Naming.ID)
public class ForgeCommonEventHandler {

    @SubscribeEvent
    public static void capabilityRegister(AttachCapabilitiesEvent<Entity> entityAttachCapabilitiesEvent) {
        if (entityAttachCapabilitiesEvent.getObject() instanceof PlayerEntity) {
            NamingCapabilityProvider provider = new NamingCapabilityProvider();
            entityAttachCapabilitiesEvent.addCapability(Naming.rl("namer"), provider);
            entityAttachCapabilitiesEvent.addListener(provider::invalidate);
        }
    }

    @SubscribeEvent
    public static void commandRegistration(RegisterCommandsEvent commandsEvent) {
        commandsEvent.getDispatcher()
                .register(NamingCommand.create());
    }

    @SubscribeEvent
    public static void serverChat(ServerChatEvent serverChatEvent) {
        serverChatEvent.getPlayer()
                .getLevel()
                .getDataStorage()
                .computeIfAbsent(ListeningWorldData::new, "spoken")
                .addSpoken(
                        serverChatEvent.getPlayer().getLevel(),
                        serverChatEvent.getMessage()
                );
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent worldTickEvent) {
        if (worldTickEvent.phase == TickEvent.Phase.END && worldTickEvent.world instanceof ServerWorld) {
            ((ServerWorld) worldTickEvent.world).getDataStorage()
                    .computeIfAbsent(ListeningWorldData::new, "spoken")
                    .tick(worldTickEvent.world);
        }
    }
}
