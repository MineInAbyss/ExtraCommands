package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import org.bukkit.GameMode

fun CommandDSLEntrypoint.gameModeCommand() {
    command("gamemode", "gm") {
        val mode: GameMode by enumArg()
        playerAction {
            player.gameMode = mode
        }
    }
}

fun gamemodeTabComplete(args: Array<out String>) = when (args.size) {
    1 -> GameMode.entries.map { it.name.lowercase() }.filter { it.startsWith(args[0], true) }
    2 -> extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[1]) }
    else -> emptyList()
}
