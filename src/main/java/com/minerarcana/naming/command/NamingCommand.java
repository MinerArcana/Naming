package com.minerarcana.naming.command;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.capability.Namer;
import com.minerarcana.naming.content.NamingText;
import com.minerarcana.naming.network.SyncNamingMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiPredicate;

public class NamingCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("naming")
                .then(Commands.literal("grant")
                        .requires(stack -> stack.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.entities())
                                .then(Commands.argument("ability", StringArgumentType.greedyString())
                                        .executes(context -> alterAbilities(context, INamer::grantAbility))
                                )
                        )
                )
                .then(Commands.literal("remove")
                        .requires(stack -> stack.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.entities())
                                .then(Commands.argument("ability", StringArgumentType.greedyString())
                                        .executes(context -> alterAbilities(context, INamer::removeAbility))
                                )
                        )
                )
                .then(Commands.literal("list")
                        .then(Commands.argument("target", EntityArgument.entity())
                                .executes(context -> listForEntity(context, EntityArgument.getEntity(context, "target")))
                        )
                        .executes(context -> listForEntity(context, context.getSource().getEntityOrException()))
                )
                .then(Commands.literal("sync")
                        .then(Commands.argument("target", EntityArgument.players())
                                .executes(context -> syncPlayers(context, EntityArgument.getPlayers(context, "target")))
                        )
                        .executes(context -> syncPlayers(
                                context,
                                Collections.singleton(context.getSource().getPlayerOrException())
                        ))
                );
    }

    private static int alterAbilities(CommandContext<CommandSourceStack> context, BiPredicate<INamer, String> alterAbilities) throws CommandSyntaxException {
        String ability = StringArgumentType.getString(context, "ability");
        return EntityArgument.getEntities(context, "target")
                .stream()
                .mapToInt(entity -> entity.getCapability(Namer.CAP)
                        .map(namer -> {
                                    if (alterAbilities.test(namer, ability)) {
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
    }

    private static int listForEntity(CommandContext<CommandSourceStack> context, Entity entity) {
        Collection<String> abilities = entity.getCapability(Namer.CAP)
                .map(INamer::getAbilities)
                .orElse(Collections.emptyList());

        if (!abilities.isEmpty()) {
            context.getSource().sendSuccess(NamingText.CURRENT_ABILITIES, true);
        }
        for (String ability : abilities) {
            context.getSource()
                    .sendSuccess(
                            Component.literal(
                                    " * " + ability
                            ),
                            true
                    );
        }
        return abilities.isEmpty() ? 0 : 1;
    }

    private static int syncPlayers(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> serverPlayers) {

        for (ServerPlayer serverPlayer : serverPlayers) {
            serverPlayer.getCapability(Namer.CAP)
                    .ifPresent(namer -> Naming.network.syncCap(
                            serverPlayer,
                            new SyncNamingMessage(namer.getAbilities())
                    ));
        }
        context.getSource().sendSuccess(
                Component.translatable(
                        NamingText.SYNCED.getString(),
                        serverPlayers.size()
                ),
                true
        );
        return serverPlayers.size();
    }
}
