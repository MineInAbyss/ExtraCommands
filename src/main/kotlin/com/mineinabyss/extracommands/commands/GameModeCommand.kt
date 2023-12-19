package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.info
import com.mineinabyss.idofront.messaging.success
import org.bukkit.GameMode

fun CommandDSLEntrypoint.gameModeCommand() {
    command("gamemode", "gm") {
        val mode: GameMode by enumArg()
        permission = "extracommands.gamemode.${mode.name.lowercase()}"

        playerAction {
            player.gameMode = mode
            sender.info("<gold>Gamemode set to <i>${mode.name.lowercase()}</i> ${"for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    command("gmc") {
        permission = "extracommands.gamemode.creative"
        playerAction {
            player.gameMode = GameMode.CREATIVE
            sender.info("<gold>Gamemode set to <i>${GameMode.CREATIVE.name.lowercase()}</i> ${"for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    command("gms") {
        permission = "extracommands.gamemode.survival"
        playerAction {
            player.gameMode = GameMode.SURVIVAL
            sender.info("<gold>Gamemode set to <i>${GameMode.SURVIVAL.name.lowercase()}</i> ${" or ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    command("gma") {
        permission = "extracommands.gamemode.adventure"
        playerAction {
            player.gameMode = GameMode.ADVENTURE
            sender.info("<gold>Gamemode set to <i>${GameMode.ADVENTURE.name.lowercase()}</i> ${" for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    command("gmsp") {
        permission = "extracommands.gamemode.spectator"
        playerAction {
            player.gameMode = GameMode.SPECTATOR
            sender.info("<gold>Gamemode set to <i>${GameMode.SPECTATOR.name.lowercase()}</i> ${" for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
}

fun gamemodeTabComplete(args: Array<out String>) = when (args.size) {
    1 -> GameMode.entries.map { it.name.lowercase() }.filter { it.startsWith(args[0], true) }
    2 -> extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[1], true) }
    else -> emptyList()
}
