package com.mineinabyss.extracommands

import com.mineinabyss.extracommands.commands.*
import com.mineinabyss.idofront.commands.brigadier.commands

object ExtraBrigadierCommands {
    fun registerCommands() {
        extraCommands.plugin.commands {
            seenCommand()
            personalWeatherCommand()
            personalTimeCommand()
            gameModeCommand()
            hungerCommand()
            healCommand()
            godCommand()
            suicideCommand()
            itemRenameCommand()
            afkCommand()
            movementCommands()
            uptimeCommand()
            scheduleRestartCommand()
            onlineInfoCommand()
            vanishCommand()
            viewDistanceCommands()

            mineInAbyssCommands()
        }
    }
}
