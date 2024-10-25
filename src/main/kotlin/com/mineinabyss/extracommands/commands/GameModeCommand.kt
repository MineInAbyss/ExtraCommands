package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.IdoCommand
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.messaging.info
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import org.bukkit.GameMode
import org.bukkit.entity.Player

fun RootIdoCommands.gameModeCommand() {
    fun IdoCommand.gamemodeShortcut(gameMode: GameMode) {
        requiresPermission("extracommands.gamemode.creative")
        executes(
            ArgumentTypes.players().resolve()
                .default { listOf(executor as? Player ?: fail("Receiver must be a player")) }) { players ->
            players.forEach { it.gameMode = gameMode }
            val playerString = players.filter { it != executor }.takeIf { it.isNotEmpty() }?.let {
                val playerList = it.joinToString(", ") { it.name }
                "for $playerList".plus(if (it.size > 5) "..." else "")
            } ?: ""
            sender.info("<gold>Gamemode set to <i>${gameMode.name.lowercase()}</i> $playerString")
        }
    }
    ("gamemode" / "gm") { GameMode.entries.forEach { it.name.lowercase().invoke { gamemodeShortcut(it) } } }
    "gmc" { gamemodeShortcut(GameMode.CREATIVE) }
    "gms" { gamemodeShortcut(GameMode.SURVIVAL) }
    "gma" { gamemodeShortcut(GameMode.ADVENTURE) }
    "gmsp" { gamemodeShortcut(GameMode.SPECTATOR) }
}
