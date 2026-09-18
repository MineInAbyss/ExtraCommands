package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.IdoCommand
import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.messaging.info
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import org.bukkit.GameMode
import org.bukkit.entity.Player

fun RootIdoCommands.gameModeCommand() {
    fun IdoCommand.gamemodeShortcut(gameMode: GameMode) {
        permission = "extracommands.gamemode.${gameMode.name.lowercase()}"
        executes.asPlayer {
            player.gameMode = gameMode
            sender.info("<gold>Gamemode set to <i>${gameMode.name.lowercase()}</i>")
        }
    }
    ("gamemode" / "gm") { GameMode.entries.forEach { it.name.lowercase().invoke { gamemodeShortcut(it) } } }
    "gmc" { gamemodeShortcut(GameMode.CREATIVE) }
    "gms" { gamemodeShortcut(GameMode.SURVIVAL) }
    "gma" { gamemodeShortcut(GameMode.ADVENTURE) }
    "gmsp" { gamemodeShortcut(GameMode.SPECTATOR) }
}
