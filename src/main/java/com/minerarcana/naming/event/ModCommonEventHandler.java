package com.minerarcana.naming.event;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.api.blockentity.NameableBlockEntityWrapper;
import com.minerarcana.naming.blockentity.NameableBlockRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

@EventBusSubscriber(modid = Naming.ID, bus = Bus.MOD)
public class ModCommonEventHandler {

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void handleIMCReceive(InterModProcessEvent event) {
        event.getIMCStream("nameable_blocks"::equals)
                .forEach(message -> {
                    BlockEntityType<?> namingBlockEntityType = null;
                    Function<BlockEntity, NameableBlockEntityWrapper<?>> naming = null;

                    Object messageContent = message.messageSupplier().get();
                    if (messageContent instanceof Pair<?, ?> pair) {
                        if (pair.getLeft() instanceof BlockEntityType<?> blockEntityType) {
                            namingBlockEntityType = blockEntityType;
                        }
                        if (pair.getRight() instanceof Function<?, ?> wrapperFunction) {
                            try {
                                naming = (Function<BlockEntity, NameableBlockEntityWrapper<?>>) wrapperFunction;
                            } catch (Throwable throwable) {
                                Naming.LOGGER.error("Failed to get Naming Predicate from IMC {} {}", message.senderModId(), messageContent.toString());
                            }
                        }
                    }

                    if (namingBlockEntityType != null && naming != null) {
                        NameableBlockRegistry.getInstance()
                                .add(namingBlockEntityType, naming);
                    } else {
                        Naming.LOGGER.error("Failed to Find BlockEntityType and Naming Handling from {}", message.senderModId());
                    }
                });
    }
}
