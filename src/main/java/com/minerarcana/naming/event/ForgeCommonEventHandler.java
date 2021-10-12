package com.minerarcana.naming.event;


import com.minerarcana.naming.Naming;
import com.minerarcana.naming.capability.NamingCapabilityProvider;
import com.minerarcana.naming.command.NamingCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
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
}
