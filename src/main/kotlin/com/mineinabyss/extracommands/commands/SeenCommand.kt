package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.offlinePlayerArg
import com.mineinabyss.idofront.commands.arguments.stringArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.textcomponents.miniMsg
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun CommandDSLEntrypoint.seenCommand() {
    command("seen") {
        val offlinePlayer: OfflinePlayer by offlinePlayerArg()
        action {
            if (offlinePlayer.lastSeen == 0L) return@action sender.error("A player with the  name ${offlinePlayer.name} has never joined the server.")
            if (offlinePlayer.isOnline) return@action sender.error("A player with the name ${offlinePlayer.name} is currently online.")

            val timeSince = calculateTime(dateDifference(Date(offlinePlayer.lastSeen)))
            sender.sendMessage(("<gold><i>" + offlinePlayer.name + "</i> was last seen " + "<yellow>" + timeSince + "</yellow> ago.").miniMsg())
        }
    }
}

fun seenTabComplete(args: Array<out String>) =
    extraCommands.plugin.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[0], true) }
        .takeIf { args.size == 1 } ?: emptyList()

fun calculateTime(s: Long) = s.toDuration(DurationUnit.MILLISECONDS).toComponents { days, hours, minutes, seconds, _ ->
    var timeSince = "$days days, $hours hours, $minutes minutes, $seconds seconds"
    if (days == 0L) timeSince = timeSince.removePrefix("0 days, ")
    if (hours == 0) timeSince = timeSince.removePrefix("0 hours, ")
    if (minutes == 0) timeSince = timeSince.removePrefix("0 minutes, ")

    timeSince
}

private fun dateDifference(seen: Date, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Long {
    val diffInMillies = Date.from(Instant.now()).time - seen.time
    return timeUnit.convert(diffInMillies, timeUnit)
}
