package com.mineinabyss.extracommands.commands

import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.geary.papermc.datastore.encodeComponentsTo
import com.mineinabyss.geary.papermc.tracking.entities.toGearyOrNull
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.arguments.DurationTypeArgument
import com.mineinabyss.idofront.plugin.Plugins
import com.mineinabyss.idofront.textcomponents.miniMsg
import com.mineinabyss.idofront.time.ticks
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import java.time.Duration.ofSeconds
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

//val maintenance by lazy { runCatching { MaintenanceProvider.get() as MaintenanceProxy }.getOrNull() }
fun RootIdoCommands.scheduleRestartCommand() {
    var currentJob: Job? = null
    "schedulestop" {
        val delay by DurationTypeArgument(10.seconds)
        executes {
            val duration = delay()
            fun showTitle(time: Duration, fade: Boolean) {
                Bukkit.getServer().showTitle(
                    Title.title(
                        "<red><bold>Server Stopping".miniMsg(),
                        "Server will stop in ${time.toComponents { d, h, m, s, _ -> "${d}d ${h}h ${m}m ${s}s" }.replace("0[a-zA-Z]\\s? ".toRegex(), "")}.".miniMsg(),
                        if (fade) Title.Times.times(ofSeconds(1), ofSeconds(5), ofSeconds(1))
                        else Title.Times.times(ofSeconds(0), ofSeconds(2), ofSeconds(0))
                    )
                )
            }
            currentJob = extraCommands.plugin.launch {
                showTitle(duration, fade = true)

                (duration - 10.seconds).takeIf(Duration::isPositive)?.let { delay(it) }

                repeat(10) {
                    showTitle((10 - it).seconds, fade = false)
                    delay(1.seconds)
                }

                //val server = maintenance?.getServer("survival")
                //val isMaintenance = maintenance?.isMaintenance(server) ?: false
                //if (!isMaintenance) maintenance?.setMaintenanceToServer(server, true)
                Bukkit.savePlayers()
                Bukkit.getWorlds().forEach { world ->
                    if (Plugins.isEnabled("Geary")) world.entities.forEach { e ->
                        e.toGearyOrNull()?.encodeComponentsTo(e.persistentDataContainer)
                    }
                    world.save()
                }
                //if (!isMaintenance) maintenance?.setMaintenanceToServer(server, false)

                Bukkit.shutdown()
            }
        }
    }
    "schedulerestart" {
        requiresPermission("extracommands.schedulerestart")
        val delay by DurationTypeArgument(10.seconds)
        executes {
            val duration = delay()
            fun showTitle(time: Duration, fade: Boolean) {
                Bukkit.getServer().showTitle(
                    Title.title(
                        "<red><bold>Server Restarting".miniMsg(),
                        "Server will restart in ${time.toComponents { d, h, m, s, _ -> "${d}d ${h}h ${m}m ${s}s" }.replace("0[a-zA-Z]\\s? ".toRegex(), "")}.".miniMsg(),
                        if (fade) Title.Times.times(ofSeconds(1), ofSeconds(5), ofSeconds(1))
                        else Title.Times.times(ofSeconds(0), ofSeconds(2), ofSeconds(0))
                    )
                )
            }
            currentJob = extraCommands.plugin.launch {
                showTitle(duration, fade = true)

                (duration - 10.seconds).takeIf(Duration::isPositive)?.let { delay(it) }

                repeat(10) {
                    showTitle((10 - it).seconds, fade = false)
                    delay(1.seconds)
                }

                //val server = maintenance?.getServer("survival")
                //val isMaintenance = maintenance?.isMaintenance(server) ?: false
                //if (!isMaintenance) maintenance?.setMaintenanceToServer(server, true)
                Bukkit.savePlayers()
                Bukkit.getWorlds().forEach { world ->
                    if (Plugins.isEnabled("Geary")) world.entities.forEach { e ->
                        e.toGearyOrNull()?.encodeComponentsTo(e.persistentDataContainer)
                    }
                    world.save()
                }
                //if (!isMaintenance) maintenance?.setMaintenanceToServer(server, false)

                Bukkit.spigot().restart()
            }
        }
    }
    "cancelrestart" {
        requiresPermission("extracommands.cancelrestart")
        executes {
            currentJob?.cancel() ?: return@executes
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
