package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mojang.brigadier.arguments.IntegerArgumentType

fun RootIdoCommands.hungerCommand() {
    "hunger" {
        val hunger by IntegerArgumentType.integer(0, 20)
        playerExecutes {
            player.foodLevel = hunger()
        }
    }
}
