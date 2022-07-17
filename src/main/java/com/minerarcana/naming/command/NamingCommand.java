package com.minerarcana.naming.command;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingText;
import com.minerarcana.naming.network.SyncNamingMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Collections;

public class NamingCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create() {
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
                                                                            if (entity instanceof ServerPlayer) {
                                                                                Naming.network.syncCap(
                                                                                        (ServerPlayer) entity,
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
                )
                .then(Commands.literal("list")
                        .then(Commands.argument("target", EntityArgument.entity())
                                .executes(context -> {
                                    Collection<String> abilities = EntityArgument.getEntity(context, "target")
                                            .getCapability(Namer.CAP)
                                            .map(INamer::getAbilities)
                                            .orElse(Collections.emptyList());

                                    if (!abilities.isEmpty()) {
                                        context.getSource().sendSuccess(NamingText.ABILITIES, true);
                                    }
                                    for (String ability : abilities) {
                                        context.getSource()
                                                .sendSuccess(
                                                        new TextComponent(
                                                                " * " + ability
                                                        ),
                                                        true
                                                );
                                    }
                                    return abilities.isEmpty() ? 0 : 1;
                                })
                        )
                );
    }
}
