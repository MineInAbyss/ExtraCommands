package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.success

fun CommandDSLEntrypoint.godCommand() {
    command("god") {
        playerAction {
            player.isInvulnerable = !player.isInvulnerable
            player.success("God mode is now ${if (player.isInvulnerable) "on" else "off"} ${if (sender == player) "" else "for ${player.name}"}")
        }
    }
}

fun godTabComplete(args: Array<out String>) =
    extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[0]) }.takeIf { args.size == 1 } ?: emptyList()
