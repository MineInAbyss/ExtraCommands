package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.arguments.offlinePlayerArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.messaging.error
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
            var lastSeenTime =
                "${timeSince.days} days ${timeSince.hours} hours ${timeSince.minutes} minutes ${timeSince.seconds} seconds"

            if (timeSince.days == 0) lastSeenTime = lastSeenTime.removePrefix("0 days ")
            if (timeSince.hours == 0L) lastSeenTime = lastSeenTime.removePrefix("0 hours ")
            if (timeSince.minutes == 0L) lastSeenTime = lastSeenTime.removePrefix("0 minutes ")

            sender.sendMessage(offlinePlayer.name + " was last seen " + lastSeenTime + " ago.")
        }
    }
}


private class TimeSince(val days: Int, val hours: Long, val minutes: Long, val seconds: Long)

private fun calculateTime(s: Long): TimeSince {
    val days = TimeUnit.MILLISECONDS.toDays(s).toInt()
    val hours = TimeUnit.MILLISECONDS.toHours(s) - TimeUnit.DAYS.toHours(days.toLong())
    val minutes = TimeUnit.MILLISECONDS.toMinutes(s) - TimeUnit.HOURS.toMinutes(days.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(s) - TimeUnit.MINUTES.toSeconds(minutes)
    return TimeSince(days, hours, minutes, seconds)
}

private fun dateDifference(seen: Date, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Long {
    val diffInMillies = Date.from(Instant.now()).time - seen.time
    return timeUnit.convert(diffInMillies, timeUnit)
}
