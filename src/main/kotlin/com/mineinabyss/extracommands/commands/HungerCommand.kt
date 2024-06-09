package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.intArg
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mojang.brigadier.arguments.IntegerArgumentType
import kotlin.math.max
import kotlin.math.min

fun RootIdoCommands.hungerCommand() {
    "hunger" {
        val hunger by IntegerArgumentType.integer(0, 20)
        playerExecutes {
            player.foodLevel = hunger()
        }
    }
}
