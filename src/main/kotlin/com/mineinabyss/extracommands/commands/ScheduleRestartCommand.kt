package com.mineinabyss.extracommands.commands

import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.textcomponents.miniMsg
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import java.time.Duration.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun CommandDSLEntrypoint.scheduleRestartCommand() {
    var currentJob: Job? = null
    command("schedulerestart") {
        val delay: Duration by arg {
            parseErrorMessage = { "Please enter a valid duration, ex 1d2h3m4s for the $name" }
            missingMessage = { "Please enter a duration for the $name" }
            parseBy { Duration.parse(passed) }
        }

        action {
            fun showTitle(time: Duration, fade: Boolean) {
                Bukkit.getServer().showTitle(
                    Title.title(
                        "<red><bold>Server Restarting".miniMsg(),
                        "Server will restart in $time".miniMsg(),
                        if (fade) Title.Times.times(ofSeconds(1), ofSeconds(5), ofSeconds(1))
                        else Title.Times.times(ofSeconds(0), ofSeconds(2), ofSeconds(0))
                    )
                )
            }
            currentJob = extraCommands.plugin.launch {
                showTitle(delay, fade = true)

                (delay - 10.seconds).takeIf { it.isPositive() }
                    ?.let { delay(it) }

                repeat(10) {
                    showTitle((10 - it).seconds, fade = false)
                    delay(1.seconds)
                }

                Bukkit.spigot().restart()
            }
        }
    }
    command("cancelrestart") {
        action {
            currentJob?.cancel()
            Bukkit.getServer().clearTitle()
            Bukkit.getServer().showTitle(
                Title.title(
                    "<green><bold>Server Restart Cancelled!".miniMsg(),
                    "Server will no longer restart...".miniMsg(),
                    Title.Times.times(ofSeconds(1), ofSeconds(5), ofSeconds(1))
                )
            )
        }
    }
}
