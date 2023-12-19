package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.arguments.genericArg
import com.mineinabyss.idofront.commands.arguments.optionArg
import com.mineinabyss.idofront.commands.arguments.playerArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.success
import org.bukkit.WeatherType
import org.bukkit.entity.Player

fun CommandDSLEntrypoint.personalWeatherCommand() {
    command("personalweather", "pweather") {
        val weather: PersonalWeatherType by enumArg()
        playerAction {
            when (weather) {
                PersonalWeatherType.RESET -> {
                    player.resetPlayerWeather()
                    player.success("Reset weather")
                }
                else -> {
                    player.setPlayerWeather(weather.toWeatherType()!!)
                    player.success("Set weather to $weather ${if (sender == player) "" else "for ${player.name}"}")
                }
            }
        }
    }
}

fun personalWeatherTabComplete(args: Array<out String>) = when (args.size) {
    1 -> PersonalWeatherType.entries.map { it.name.lowercase() }.filter { it.startsWith(args[0], true) }
    2 -> extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[1], true) }
    else -> emptyList()
}

private enum class PersonalWeatherType {
    SUN, CLEAR, STORM, THUNDER, RESET;

    fun toWeatherType(): WeatherType? = when(this) {
        SUN, CLEAR -> WeatherType.CLEAR
        STORM, THUNDER -> WeatherType.DOWNFALL
        RESET -> null
    }
}
