package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.messaging.success
import com.mojang.brigadier.arguments.StringArgumentType
import org.bukkit.WeatherType
import org.bukkit.entity.Player

fun RootIdoCommands.personalWeatherCommand() {
    ("personalweather" / "pweather") {
        executes.asPlayer().args("weather" to StringArgumentType.word().suggests { suggestFiltering(PersonalWeatherType.entries.map { it.name.lowercase() }) }
            .map { PersonalWeatherType.entries.firstOrNull { w -> w.name.equals(it, ignoreCase = true) } ?: fail("Unknown weather $it") }
        ) { weather ->
            when (weather) {
                PersonalWeatherType.RESET -> {
                    player.resetPlayerWeather()
                    player.success("Reset weather")
                }
                else -> {
                    player.setPlayerWeather(weather.toWeatherType()!!)
                    player.success("Set weather to ${weather.name.lowercase()}${if (sender == player) "" else " for ${player.name}"}")
                }
            }
        }
    }
}

private enum class PersonalWeatherType {
    SUN, CLEAR, STORM, THUNDER, RESET;

    fun toWeatherType(): WeatherType? = when(this) {
        SUN, CLEAR -> WeatherType.CLEAR
        STORM, THUNDER -> WeatherType.DOWNFALL
        RESET -> null
    }
}
