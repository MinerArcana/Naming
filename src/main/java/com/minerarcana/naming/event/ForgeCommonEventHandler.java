package com.minerarcana.naming.event;


import com.minerarcana.naming.Naming;
import com.minerarcana.naming.advancement.criteria.messaged.MessageTarget;
import com.minerarcana.naming.api.capability.INameable;
import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.blockentity.ListeningType;
import com.minerarcana.naming.capability.Nameable;
import com.minerarcana.naming.capability.NameableCapabilityProvider;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.capability.NamingCapabilityProvider;
import com.minerarcana.naming.command.NamingCommand;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.minerarcana.naming.network.SyncNamingMessage;
import com.minerarcana.naming.worlddata.ListeningWorldData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Naming.ID)
public class ForgeCommonEventHandler {

    @SubscribeEvent
    public static void capabilityRegister(AttachCapabilitiesEvent<Entity> entityAttachCapabilitiesEvent) {
        if (entityAttachCapabilitiesEvent.getObject() instanceof Player) {
            NamingCapabilityProvider provider = new NamingCapabilityProvider();
            entityAttachCapabilitiesEvent.addCapability(Naming.rl("namer"), provider);
        } else if (entityAttachCapabilitiesEvent.getObject() instanceof LivingEntity) {
            NameableCapabilityProvider provider = new NameableCapabilityProvider();
            entityAttachCapabilitiesEvent.addCapability(Naming.rl("nameable"), provider);
        }
    }

    @SubscribeEvent
    public static void playerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(Namer.CAP)
                    .ifPresent(cap -> {
                        if (cap instanceof Namer) {
                            Naming.network.syncCap(
                                    serverPlayer,
                                    new SyncNamingMessage(cap.getAbilities())
                            );
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone cloneEvent) {
        cloneEvent.getOriginal()
                .reviveCaps();

        LazyOptional<INamer> originalNamer = cloneEvent.getOriginal()
                .getCapability(Namer.CAP);

        originalNamer.ifPresent(cap -> {
            CompoundTag compoundNBT = cap.serializeNBT();
            cloneEvent.getEntity()
                    .getCapability(Namer.CAP)
                    .ifPresent(newCap -> newCap.deserializeNBT(compoundNBT));
        });

        originalNamer.invalidate();
        cloneEvent.getOriginal()
                .invalidateCaps();
    }

    @SubscribeEvent
    public static void commandRegistration(RegisterCommandsEvent commandsEvent) {
        commandsEvent.getDispatcher()
                .register(NamingCommand.create());
    }

    @SubscribeEvent
    public static void serverChat(ServerChatEvent serverChatEvent) {
        ServerPlayer player = serverChatEvent.getPlayer();
        ListeningType listeningType = player.getLevel()
                .getDataStorage()
                .computeIfAbsent(ListeningWorldData::new, ListeningWorldData::new, ListeningWorldData.NAME)
                .speakTo(serverChatEvent.getMessage().toString());

        if (listeningType.isListening()) {
            player.getCapability(Namer.CAP)
                    .ifPresent(namer -> namer.heardBy(serverChatEvent.getMessage().toString()));
            NamingCriteriaTriggers.MESSAGED.trigger(player, serverChatEvent.getMessage().toString(), MessageTarget.HEARD_BY);
        }
        if (listeningType.isConsuming()) {
            serverChatEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.LevelTickEvent levelTickEvent) {
        Level world = levelTickEvent.level;
        if (levelTickEvent.phase == TickEvent.Phase.END && world instanceof ServerLevel) {
            DimensionDataStorage manager = ((ServerLevel) world).getDataStorage();
            if (world.random.nextInt(100) == 0) {
                manager.computeIfAbsent(ListeningWorldData::new, ListeningWorldData::new, ListeningWorldData.NAME)
                        .clean();
            }
        }
    }

    @SubscribeEvent
    public static void livingEntityDeath(LivingDeathEvent livingDeathEvent) {
        if (livingDeathEvent.getEntity().getLevel() instanceof ServerLevel serverLevel &&
                serverLevel.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
            LivingEntity livingEntity = livingDeathEvent.getEntity();
            livingEntity.getCapability(Nameable.CAP)
                    .filter(INameable::isMagicallyNamed)
                    .ifPresent(nameable -> {
                        if (!(livingEntity instanceof TamableAnimal animal) ||
                                animal.getOwnerUUID() != nameable.getNamerUUID()) {
                            Player player = serverLevel.getPlayerByUUID(nameable.getNamerUUID());
                            if (player != null) {
                                Component deathMessage = livingEntity.getCombatTracker()
                                        .getDeathMessage();

                                player.sendSystemMessage(deathMessage);
                            }
                        }
                    });
        }
    }
}
