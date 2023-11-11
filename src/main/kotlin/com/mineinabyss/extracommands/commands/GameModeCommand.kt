package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import org.bukkit.GameMode

fun CommandDSLEntrypoint.gameModeCommand() {
    command("gamemode") {
        val mode: GameMode by enumArg()
        playerAction {
            player.gameMode = mode
        }
    }
}
