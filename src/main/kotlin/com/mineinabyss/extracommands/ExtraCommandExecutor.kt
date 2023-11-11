package com.mineinabyss.extracommands

import com.mineinabyss.extracommands.commands.*
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor

class ExtraCommandExecutor : IdofrontCommandExecutor() {
    override val commands = commands(extraCommands.plugin) {
        seenCommand()
        personalWeatherCommand()
        personalTimeCommand()
        gameModeCommand()
        hungerCommand()
        godCommand()
    }
}
