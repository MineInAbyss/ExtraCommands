package com.mineinabyss.extracommands.commands

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.extracommands.listeners.SeenListener
import com.mineinabyss.idofront.commands.arguments.offlinePlayerArg
import com.mineinabyss.idofront.commands.arguments.stringArg
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.info
import com.mineinabyss.idofront.textcomponents.miniMsg
import com.mojang.brigadier.arguments.StringArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import kotlinx.coroutines.yield
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration


fun RootIdoCommands.seenCommand() {
    "seen" {
        val playername by StringArgumentType.word().suggests {
            suggest(SeenListener.previouslyOnline.toList())
        }
        playerExecutes {
            val playerName = playername()!!
            var player = Bukkit.getPlayerExact(playerName) as? OfflinePlayer

            SeenListener.currentlyQuerying[sender]?.let {
                return@playerExecutes sender.error("You are currently looking up another player, waiting for lookup to finish...")
            }

            if (player == null) {
                SeenListener.currentlyQuerying[sender] = extraCommands.plugin.launch(extraCommands.plugin.asyncDispatcher) {
                    player = Bukkit.getOfflinePlayerIfCached(playerName)
                    if (player == null) player = Bukkit.getOfflinePlayer(playerName)
                }.also {
                    it.invokeOnCompletion { SeenListener.currentlyQuerying.remove(sender) }
                }
            }

            if (player == null || !player!!.hasPlayedBefore()) return@playerExecutes sender.error("A player with the  name $playerName has never joined the server.")
            if (player!!.isOnline) return@playerExecutes sender.error("A player with the name $playerName is currently online.")


            val timeSince = calculateTime(dateDifference(Date(player!!.lastSeen)))
            sender.info("<gold><i>$playerName</i> was last seen <yellow>$timeSince</yellow> ago.")
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
