package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.offlinePlayerArg
import com.mineinabyss.idofront.commands.arguments.stringArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.messaging.error
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

fun CommandDSLEntrypoint.seenCommand() {
    command("seen") {
        val offlinePlayer: OfflinePlayer by offlinePlayerArg()
        action {
            if (offlinePlayer.lastSeen == 0L) return@action sender.error("A player with the  name ${offlinePlayer.name} has never joined the server.")
            if (offlinePlayer.isOnline) return@action sender.error("A player with the name ${offlinePlayer.name} is currently online.")

            val timeSince = calculateTime(dateDifference(Date(offlinePlayer.lastSeen)))
            sender.sendMessage(offlinePlayer.name + " was last seen " + timeSince.toString() + " ago.")
        }
    }
}

fun seenTabComplete(args: Array<out String>) =
    extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[0], true) }
        .takeIf { args.size == 1 } ?: emptyList()


class TimeSince(val days: Int, val hours: Long, val minutes: Long, val seconds: Long) {
    override fun toString(): String {
        var timeSince = "$days days $hours hours $minutes minutes $seconds seconds"
        if (this.days == 0) timeSince = timeSince.removePrefix("0 days ")
        if (this.hours == 0L) timeSince = timeSince.removePrefix("0 hours ")
        if (this.minutes == 0L) timeSince = timeSince.removePrefix("0 minutes ")

        return timeSince
    }
}

fun calculateTime(s: Long): TimeSince {
    val days = TimeUnit.MILLISECONDS.toDays(s).toInt()
    val hours = TimeUnit.MILLISECONDS.toHours(s) - TimeUnit.DAYS.toHours(days.toLong())
    val minutes = TimeUnit.MILLISECONDS.toMinutes(s) - TimeUnit.HOURS.toMinutes(hours)
    val seconds =
        TimeUnit.MILLISECONDS.toSeconds(s) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes)
    return TimeSince(days, hours, minutes, seconds)
}

private fun dateDifference(seen: Date, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Long {
    val diffInMillies = Date.from(Instant.now()).time - seen.time
    return timeUnit.convert(diffInMillies, timeUnit)
}
