package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.info
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import org.bukkit.GameMode

fun RootIdoCommands.gameModeCommand() {
    ("gamemode"/  "gm") {
        val gameMode by ArgumentTypes.gameMode().suggests {
            suggest(GameMode.entries.map { it.name.lowercase() })
        }
        requiresPermission("extracommands.gamemode.${gameMode.name.lowercase()}")
        playerExecutes {
            val gameMode = gameMode() ?: return@playerExecutes
            player.gameMode = gameMode
            sender.info("<gold>Gamemode set to <i>${gameMode.name.lowercase()}</i> ${"for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    "gmc" {
        requiresPermission("extracommands.gamemode.creative")
        playerExecutes {
            player.gameMode = GameMode.CREATIVE
            sender.info("<gold>Gamemode set to <i>${GameMode.CREATIVE.name.lowercase()}</i> ${"for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    "gms" {
        requiresPermission("extracommands.gamemode.survival")
        playerExecutes {
            player.gameMode = GameMode.SURVIVAL
            sender.info("<gold>Gamemode set to <i>${GameMode.SURVIVAL.name.lowercase()}</i> ${" or ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    "gma" {
        requiresPermission("extracommands.gamemode.adventure")
        playerExecutes {
            player.gameMode = GameMode.ADVENTURE
            sender.info("<gold>Gamemode set to <i>${GameMode.ADVENTURE.name.lowercase()}</i> ${" for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    "gmsp" {
        requiresPermission("extracommands.gamemode.spectator")
        playerExecutes {
            player.gameMode = GameMode.SPECTATOR
            sender.info("<gold>Gamemode set to <i>${GameMode.SPECTATOR.name.lowercase()}</i> ${" for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
}
