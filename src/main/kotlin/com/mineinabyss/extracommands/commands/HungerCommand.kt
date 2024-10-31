package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.commands.brigadier.playerExecutes
import com.mojang.brigadier.arguments.IntegerArgumentType

fun RootIdoCommands.hungerCommand() {
    "hunger" {
        playerExecutes(
            IntegerArgumentType.integer(0, 20),
        ) { hunger ->
            player.foodLevel = hunger
        }
    }
}
