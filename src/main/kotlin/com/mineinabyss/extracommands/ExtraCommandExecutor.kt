package com.mineinabyss.extracommands

import com.mineinabyss.extracommands.commands.*
import com.mineinabyss.idofront.commands.execution.IdofrontCommandExecutor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class ExtraCommandExecutor : IdofrontCommandExecutor(), TabCompleter {
    override val commands = commands(extraCommands.plugin) {
        seenCommand()
        personalWeatherCommand()
        personalTimeCommand()
        gameModeCommand()
        hungerCommand()
        godCommand()
        suicideCommand()
        itemRenameCommand()
        afkCommand()

        mineInAbyssCommands()
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        return when (label) {
            "seen" -> return seenTabComplete(args)
            "personalweather", "pweather" -> personalWeatherTabComplete(args)
            "personaltime", "ptime" -> personalTimeTabComplete(args)
            "gamemode", "gm" -> gamemodeTabComplete(args)
            "hunger" -> hungerTabComplete(args)
            "god" -> godTabComplete(args)
            else -> emptyList()
        }
    }
}
