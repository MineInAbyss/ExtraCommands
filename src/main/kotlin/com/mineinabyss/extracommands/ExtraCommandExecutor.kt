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
        healCommand()
        godCommand()
        suicideCommand()
        itemRenameCommand()
        afkCommand()
        movementCommands()
        uptimeCommand()
        scheduleRestartCommand()
        mapCommands()

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
            "gmc", "gms", "gma", "gmsp" -> extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[0], true) }
            "hunger" -> hungerTabComplete(args)
            "god" -> godTabComplete(args)
            "itemrename" -> itemRenameTabComplete(sender, args)
            "maps" -> listOf("delete", "info")
            else -> emptyList()
        }
    }
}
