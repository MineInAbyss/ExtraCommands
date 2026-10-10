package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.messaging.success
import com.mojang.brigadier.arguments.StringArgumentType
import org.bukkit.entity.Player

fun RootIdoCommands.personalTimeCommand() {
    ("personaltime" / "ptime") {
        executes.asPlayer().args(
            "time" to StringArgumentType.word().suggests { suggestFiltering(PersonalTimeType.entries.map { it.name.lowercase() }) }
                .map { PersonalTimeType.entries.firstOrNull { type -> type.name.equals(it, ignoreCase = true) } ?: fail("Unknown time $it") },
        ) { time ->
            setPlayerTime(player, time.tick)
            val target = if (sender == player) "" else " for ${player.name}"
            when (time.tick) {
                null -> player.success("Reset time$target")
                else -> player.success("Set time to ${time.name.lowercase()}$target")
            }
        }
    }
}

private fun setPlayerTime(player: Player, ticks: Long?) {
    ticks?.let {
        var time = player.playerTime
        time -= time % 24000
        time += 24000 + ticks
        time -= player.world.time
        player.setPlayerTime(time, true)
    } ?: player.resetPlayerTime()
}

private enum class PersonalTimeType(val tick: Long?) {
    SUNRISE(23000), DAWN(23000),
    DAY_START(0), DAY(0),
    MORNING(1000),
    MIDDAY(6000), NOON(6000),
    AFTERNOON(9000),
    SUNSET(12000), DUSK(12000), SUNDOWN(12000), NIGHTFALL(12000),
    NIGHT_START(14000), NIGHT(14000),
    MIDNIGHT(18000),
    RESET(null)
    ;
}
