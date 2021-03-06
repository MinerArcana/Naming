package com.minerarcana.naming.event;


import com.minerarcana.naming.Naming;
import com.minerarcana.naming.advancement.criteria.messaged.MessageTarget;
import com.minerarcana.naming.blockentity.ListeningType;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.capability.NamingCapabilityProvider;
import com.minerarcana.naming.command.NamingCommand;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.minerarcana.naming.network.SyncNamingMessage;
import com.minerarcana.naming.worlddata.EchoingWorldData;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
    public static void playerJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            event.getEntity().getCapability(Namer.CAP)
                    .ifPresent(cap -> {
                        if (cap instanceof Namer) {
                            Naming.network.syncCap(
                                    (ServerPlayerEntity) event.getEntity(),
                                    new SyncNamingMessage(cap.getAbilities())
                            );
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone cloneEvent) {
        cloneEvent.getOriginal().getCapability(Namer.CAP)
                .ifPresent(cap -> {
                    CompoundNBT compoundNBT = cap.serializeNBT();
                    cloneEvent.getPlayer().getCapability(Namer.CAP)
                            .ifPresent(newCap -> newCap.deserializeNBT(compoundNBT));
                });
    }

    @SubscribeEvent
    public static void commandRegistration(RegisterCommandsEvent commandsEvent) {
        commandsEvent.getDispatcher()
                .register(NamingCommand.create());
    }

    @SubscribeEvent
    public static void serverChat(ServerChatEvent serverChatEvent) {
        ServerPlayerEntity player = serverChatEvent.getPlayer();
        ListeningType listeningType = player.getLevel()
                .getDataStorage()
                .computeIfAbsent(ListeningWorldData::new, ListeningWorldData.NAME)
                .speakTo(serverChatEvent.getMessage());

        if (listeningType.isListening()) {
            player.getCapability(Namer.CAP)
                    .ifPresent(namer -> namer.heardBy(serverChatEvent.getMessage()));
            NamingCriteriaTriggers.MESSAGED.trigger(player, serverChatEvent.getMessage(), MessageTarget.HEARD_BY);
        }
        if (listeningType.isConsuming()) {
            serverChatEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent worldTickEvent) {
        World world = worldTickEvent.world;
        if (worldTickEvent.phase == TickEvent.Phase.END && world instanceof ServerWorld) {
            DimensionSavedDataManager manager = ((ServerWorld) world).getDataStorage();
            manager.computeIfAbsent(EchoingWorldData::new, EchoingWorldData.NAME)
                    .tick(world);
            if (world.random.nextInt(100) == 0) {
                manager.computeIfAbsent(ListeningWorldData::new, ListeningWorldData.NAME)
                        .clean();
            }
        }
    }
}
