package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.info
import com.mineinabyss.idofront.messaging.success
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import org.bukkit.GameMode

fun RootIdoCommands.gameModeCommand() {
    ("gamemode"/  "gm") {
        val gameMode by ArgumentTypes.gameMode().suggests {
            suggest(GameMode.entries.map { it.name.lowercase() })
        }
        /*requires {
            executor?.hasPermission("extracommands.gamemode.${gameMode.name.lowercase()}") == true
        }*/
        playerExecutes {
            val gameMode = gameMode() ?: return@playerExecutes
            player.gameMode = gameMode
            sender.info("<gold>Gamemode set to <i>${gameMode.name.lowercase()}</i> ${"for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    "gmc" {
        requires {
            executor?.hasPermission("extracommands.gamemode.creative") == true
        }
        playerExecutes {
            player.gameMode = GameMode.CREATIVE
            sender.info("<gold>Gamemode set to <i>${GameMode.CREATIVE.name.lowercase()}</i> ${"for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    "gms" {
        requires {
            executor?.hasPermission("extracommands.gamemode.survival") == true
        }
        playerExecutes {
            player.gameMode = GameMode.SURVIVAL
            sender.info("<gold>Gamemode set to <i>${GameMode.SURVIVAL.name.lowercase()}</i> ${" or ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    "gma" {
        requires {
            executor?.hasPermission("extracommands.gamemode.adventure") == true
        }
        playerExecutes {
            player.gameMode = GameMode.ADVENTURE
            sender.info("<gold>Gamemode set to <i>${GameMode.ADVENTURE.name.lowercase()}</i> ${" for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
    "gmsp" {
        requires {
            executor?.hasPermission("extracommands.gamemode.spectator") == true
        }
        playerExecutes {
            player.gameMode = GameMode.SPECTATOR
            sender.info("<gold>Gamemode set to <i>${GameMode.SPECTATOR.name.lowercase()}</i> ${" for ${player.name}".takeIf { sender != player } ?: ""}")
        }
    }
}
