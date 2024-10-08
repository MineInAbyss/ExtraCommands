package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.success

fun RootIdoCommands.godCommand() {
    "god" {
        requiresPermission("extracommands.god")
        playerExecutes {
            player.isInvulnerable = !player.isInvulnerable
            player.success("God mode is now ${if (player.isInvulnerable) "on" else "off"} ${if (sender == player) "" else "for ${player.name}"}")
        }
    }
}

fun godTabComplete(args: Array<out String>) =
    extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[0], true) }.takeIf { args.size == 1 } ?: emptyList()
