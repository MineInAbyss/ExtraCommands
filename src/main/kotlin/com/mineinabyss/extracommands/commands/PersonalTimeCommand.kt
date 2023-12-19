package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.arguments.genericArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.success
import org.bukkit.entity.Player

fun CommandDSLEntrypoint.personalTimeCommand() {
    command("personaltime", "ptime") {
        val time: PersonalTimeType by enumArg()
        playerAction {
            setPlayerTime(player, time.tick)
            player.success("Set time to $time ticks ${if (sender == player) "" else "for ${player.name}"}")
        }
    }
}

fun personalTimeTabComplete(args: Array<out String>) = when (args.size) {
    1 -> PersonalTimeType.entries.map { it.name.lowercase() }.filter { it.startsWith(args[0], true) }
    2 -> extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[1], true) }
    else -> emptyList()
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
