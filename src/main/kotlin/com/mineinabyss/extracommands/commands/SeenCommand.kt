package com.mineinabyss.extracommands.commands

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.extracommands.listeners.SeenListener
import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.info
import com.mojang.brigadier.arguments.StringArgumentType
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration


fun RootIdoCommands.seenCommand() {
    "seen" {
        executes.args("player" to StringArgumentType.word().suggests { suggest(SeenListener.previouslyOnline.toList()) }) { player ->
            if (Bukkit.getPlayerExact(player) != null) return@args sender.error("A player with the name $player is currently online.")

            SeenListener.currentlyQuerying[sender]?.also {
                return@args sender.error("You are currently looking up another player, waiting for lookup to finish...")
            }

            val plugin = extraCommands.plugin
            SeenListener.currentlyQuerying[sender] = plugin.launch(plugin.asyncDispatcher) {
                val offlinePlayer = Bukkit.getOfflinePlayerIfCached(player) ?: Bukkit.getOfflinePlayer(player)
                withContext(plugin.minecraftDispatcher) {
                    if (!offlinePlayer.hasPlayedBefore()) return@withContext sender.error("A player with the name $player has never joined the server.")
                    if (offlinePlayer.isOnline) return@withContext sender.error("A player with the name $player is currently online.")

                    val timeSince = calculateTime(dateDifference(Date(offlinePlayer.lastSeen)))
                    sender.info("<gold><i>$player</i> was last seen <yellow>$timeSince</yellow> ago.")
                }
            }.also { it.invokeOnCompletion { SeenListener.currentlyQuerying.remove(sender) } }
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
