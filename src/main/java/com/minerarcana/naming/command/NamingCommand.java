package com.minerarcana.naming.command;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.network.SyncNamingMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class NamingCommand {
    public static LiteralArgumentBuilder<CommandSource> create() {
        return Commands.literal("naming")
                .then(Commands.literal("grant")
                        .then(Commands.argument("target", EntityArgument.entities())
                                .then(Commands.argument("ability", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String ability = StringArgumentType.getString(context, "ability");
                                            return EntityArgument.getEntities(context, "target")
                                                    .stream()
                                                    .mapToInt(entity -> entity.getCapability(Namer.CAP)
                                                            .map(namer -> {
                                                                        if (namer.grantAbility(ability)) {
                                                                            if (entity instanceof ServerPlayerEntity) {
                                                                                Naming.network.syncCap(
                                                                                        (ServerPlayerEntity) entity,
                                                                                        new SyncNamingMessage(namer.getAbilities())
                                                                                );
                                                                            }

                                                                            return 1;
                                                                        } else {
                                                                            return 0;
                                                                        }
                                                                    }
                                                            ).orElse(0)
                                                    )
                                                    .sum();
                                        })
                                )
                        )
                );
    }
}
