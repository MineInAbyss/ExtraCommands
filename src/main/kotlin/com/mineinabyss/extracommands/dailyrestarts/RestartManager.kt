package com.mineinabyss.extracommands.dailyrestarts

import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.geary.papermc.datastore.encodeComponentsTo
import com.mineinabyss.geary.papermc.tracking.entities.toGearyOrNull
import com.mineinabyss.idofront.plugin.Plugins
import com.mineinabyss.idofront.textcomponents.miniMsg
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toKotlinDuration

class RestartManager(
    val config: DailyRestartsConfig,
) {
    var dailyRestartJob: Job? = null
    var currentJob: Job? = null

    fun scheduleDailyRestartIfEnabled() {
        if (!config.enabled) return

        val now = ZonedDateTime.now(runCatching { ZoneId.of(config.timeZone) }.getOrDefault(ZoneId.systemDefault()))
        val nextRun = now
            .withHour(config.hour)
            .withMinute(config.minute)
            .withSecond(0)
            .let { if (now > it) it.plusDays(1) else it }
        val delay = Duration.between(now, nextRun).toKotlinDuration()
        println("Scheduling daily restart at $nextRun (in $delay)")
        dailyRestartJob?.cancel()
        dailyRestartJob = restartJob(showTitleAtStart = false, delay)
    }

    fun showTitle(
        fade: Boolean,
        title: String,
        subtitle: String,
    ) {
        Bukkit.getServer().showTitle(
            Title.title(
                title.miniMsg(),
                subtitle.miniMsg(),
                if (fade) Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(5), Duration.ofSeconds(1))
                else Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(2), Duration.ofSeconds(0))
            )
        )
    }

    fun delayToString(duration: kotlin.time.Duration) = duration
        .toComponents { d, h, m, s, _ -> "${d}d ${h}h ${m}m ${s}s" }
        .replace("0[a-zA-Z]\\s? ".toRegex(), "")

    fun scheduleStop(showTitleAtStart: Boolean, duration: kotlin.time.Duration) {
        currentJob?.cancel()
        currentJob = scheduleJob(
            showTitleAtStart = showTitleAtStart,
            duration = duration,
            title = { "<red><bold>Server Stopping" },
            subtitle = { "Server will stop in ${delayToString(it)}." },
            earlyWarning = { "<red><bold>[Server] stopping in ${delayToString(it)}." },
            run = { Bukkit.shutdown() }
        )
    }

    private fun restartJob(showTitleAtStart: Boolean, duration: kotlin.time.Duration) = scheduleJob(
        showTitleAtStart = showTitleAtStart,
        duration = duration,
        title = { "<red><bold>Server Restarting" },
        subtitle = { "Server will restart in ${delayToString(it)}." },
        earlyWarning = { "<red><bold>[Server] restarting in ${delayToString(it)}." },
        run = { Bukkit.restart() }
    )

    fun scheduleRestart(showTitleAtStart: Boolean, duration: kotlin.time.Duration) {
        currentJob?.cancel()
        currentJob = restartJob(showTitleAtStart, duration)
    }

    fun cancelJob(): Boolean {
        currentJob?.cancel()
        val present = currentJob != null
        currentJob = null
        if (present) {
            Bukkit.getServer().clearTitle()
            Bukkit.getServer().showTitle(
                Title.title(
                    "<green><bold>Server Restart Cancelled!".miniMsg(),
                    "Server will no longer restart...".miniMsg(),
                    Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(5), Duration.ofSeconds(1))
                )
            )
        }
        return present
    }

    fun cancelDailyJob(): Boolean {
        dailyRestartJob?.cancel()
        val present = dailyRestartJob != null
        dailyRestartJob = null
        return present
    }

    private fun scheduleJob(
        showTitleAtStart: Boolean,
        duration: kotlin.time.Duration,
        earlyWarning: (kotlin.time.Duration) -> String,
        title: (kotlin.time.Duration) -> String,
        subtitle: (kotlin.time.Duration) -> String,
        run: suspend () -> Unit,
    ): Job = extraCommands.plugin.launch {
        if (showTitleAtStart) showTitle(fade = true, title(duration), subtitle(duration))
        var rem = duration

        suspend fun mark(time: kotlin.time.Duration, exec: suspend () -> Unit) {
            if (rem >= time) {
                delay(rem - time)
                rem = time
                exec()
            }
        }

        mark(30.minutes, {
            Bukkit.getServer().sendMessage(earlyWarning(rem).miniMsg())
        })

        mark(10.minutes, {
            Bukkit.getServer().sendMessage(earlyWarning(rem).miniMsg())
        })

        mark(1.minutes, {
            Bukkit.getServer().sendMessage(earlyWarning(rem).miniMsg())
        })

        mark(30.seconds, {
            Bukkit.getServer().sendMessage(earlyWarning(rem).miniMsg())
        })


        // Always show last 10 second countdown
        (rem - 10.seconds).takeIf(kotlin.time.Duration::isPositive)?.let { delay(it) }
        repeat(10) {
            val left = (10 - it).seconds
            showTitle(fade = false, title(left), subtitle(left))
            delay(1.seconds)
        }

        Bukkit.savePlayers()
        Bukkit.getWorlds().forEach { world ->
            if (Plugins.isEnabled("Geary")) world.entities.forEach { e ->
                e.toGearyOrNull()?.encodeComponentsTo(e.persistentDataContainer)
            }
            world.save()
        }

        run()
    }
}