package com.minerarcana.naming.command;

import com.minerarcana.naming.capability.Namer;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;

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
                                                    .map(entity -> entity.getCapability(Namer.CAP))
                                                    .mapToInt(lazyOptional -> lazyOptional.map(namer ->
                                                            namer.grantAbility(ability) ? 1 : 0
                                                    ).orElse(0))
                                                    .sum();
                                        })
                                )
                        )
                );
    }
}
