package com.mineinabyss.extracommands.commands

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.extracommands.listeners.SeenListener
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.info
import com.mojang.brigadier.arguments.StringArgumentType
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration


fun RootIdoCommands.seenCommand() {
    "seen" {
        executes(StringArgumentType.word().suggests { suggest(SeenListener.previouslyOnline.toList()) }) { player ->
            var offlinePlayer = Bukkit.getPlayerExact(player) as? OfflinePlayer

            SeenListener.currentlyQuerying[sender]?.let {
                return@executes sender.error("You are currently looking up another player, waiting for lookup to finish...")
            }

            if (offlinePlayer == null) {
                SeenListener.currentlyQuerying[sender] =
                    extraCommands.plugin.launch(extraCommands.plugin.asyncDispatcher) {
                        offlinePlayer = Bukkit.getOfflinePlayerIfCached(player)
                        if (offlinePlayer == null) offlinePlayer = Bukkit.getOfflinePlayer(player)
                    }.also {
                        it.invokeOnCompletion { SeenListener.currentlyQuerying.remove(sender) }
                    }
            }

            if (offlinePlayer == null || !offlinePlayer!!.hasPlayedBefore()) return@executes sender.error("A player with the  name $player has never joined the server.")
            if (offlinePlayer!!.isOnline) return@executes sender.error("A player with the name $player is currently online.")


            val timeSince = calculateTime(dateDifference(Date(offlinePlayer!!.lastSeen)))
            sender.info("<gold><i>$player</i> was last seen <yellow>$timeSince</yellow> ago.")
        }
    }
}

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
