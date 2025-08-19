package com.mineinabyss.extracommands.commands

import ca.spottedleaf.moonrise.common.util.MoonriseConstants
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.messaging.error
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver

fun RootIdoCommands.expCommand() {
    "exp" {
        "add" {
            executes(
                ArgumentTypes.players().named("players").resolve(),
                IntegerArgumentType.integer().named("amount").default { 0 },
                StringArgumentType.word().named("type").default { "points" }.suggests { suggest(listOf("levels", "points")) }
            ) { players, amount, type ->
                when (type) {
                    "levels" -> players.forEach { player -> player.giveExpLevels(amount) }
                    "points" -> players.forEach { player -> player.giveExp(amount) }
                    else -> sender.error("Invalid type $type, must be levels or points..")
                }
            }
        }
    }
}