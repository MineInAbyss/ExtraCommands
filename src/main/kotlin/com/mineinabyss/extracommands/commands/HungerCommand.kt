package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.intArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import kotlin.math.max
import kotlin.math.min

fun CommandDSLEntrypoint.hungerCommand() {
    command("hunger") {
        val hunger: Int by intArg()
        playerAction {
            player.foodLevel = max(0, min(hunger, 20))
        }
    }
}

fun hungerTabComplete(args: Array<out String>) =
    when (args.size) {
        1 -> (0..20).map { it.toString() }.filter { it.startsWith(args[0]) }
        2 -> extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[1], true) }
        else -> emptyList()
    }
