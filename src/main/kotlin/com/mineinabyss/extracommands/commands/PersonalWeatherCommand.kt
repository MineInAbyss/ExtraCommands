package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.commands.brigadier.playerExecutes
import com.mineinabyss.idofront.messaging.success
import com.mojang.brigadier.arguments.StringArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import org.bukkit.WeatherType
import org.bukkit.entity.Player

fun RootIdoCommands.personalWeatherCommand() {
    ("personalweather" / "pweather") {
        playerExecutes(StringArgumentType.word().suggests { PersonalWeatherType.entries.map { it.name.lowercase() } }.map( { PersonalWeatherType.valueOf(it) })) { weather ->
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

private enum class PersonalWeatherType {
    SUN, CLEAR, STORM, THUNDER, RESET;

    fun toWeatherType(): WeatherType? = when(this) {
        SUN, CLEAR -> WeatherType.CLEAR
        STORM, THUNDER -> WeatherType.DOWNFALL
        RESET -> null
    }
}
