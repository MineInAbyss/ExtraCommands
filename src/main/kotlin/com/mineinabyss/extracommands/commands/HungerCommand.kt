package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.*
import com.mojang.brigadier.arguments.IntegerArgumentType

fun RootIdoCommands.hungerCommand() {
    "hunger" {
        executes.asPlayer().args(
            "hunger" to IntegerArgumentType.integer(0, 20),
        ) { hunger ->
            player.foodLevel = hunger
        }
    }
}
