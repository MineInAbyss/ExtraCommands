package com.mineinabyss.extracommands.commands

import ca.spottedleaf.moonrise.common.PlatformHooks
import ca.spottedleaf.moonrise.common.util.MoonriseConstants
import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.arguments.DurationTypeArgument
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.messaging.success
import com.mojang.brigadier.arguments.IntegerArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import kotlinx.coroutines.delay
import org.bukkit.entity.Player
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun RootIdoCommands.viewDistanceCommands() {

    "viewdistance" {
        executes(
            ArgumentTypes.players().resolve(),
            IntegerArgumentType.integer(0, MoonriseConstants.MAX_VIEW_DISTANCE).named("viewDistance").default { 0 },
            DurationTypeArgument(1.seconds).named("duration").default { Duration.INFINITE }
        ) { players, viewDistance, duration, ->
            if (viewDistance == 0 && duration == Duration.INFINITE) {
                val viewDistances = players.groupBy { it.viewDistance }.entries.sortedBy { it.value.size }
                if (viewDistances.size == 1) {
                    sender.success("ViewDistances for all players: ${viewDistances.first().key}")
                } else sender.success(buildString {
                    append("ViewDistances for ${viewDistances.size} players:\n")
                    viewDistances.forEach { (vd, players) ->
                        if (players.size > 5) append("${players.take(5).joinToString { it.name }},...: $vd\n")
                        else append("${players.joinToString { it.name }}: $vd\n")
                    }
                })
            } else {
                players.forEach { it.viewDistance = viewDistance.takeIf { it >= 2 } ?: it.world.viewDistance }
                sender.success("Successfully set ${if (players.size == 1) "<dark_green>${players.first().name}</dark_green>" else players.size.toString() + "players"} viewDistance to <dark_green><i>$viewDistance")
                if (duration < Duration.INFINITE) {
                    extraCommands.plugin.launch {
                        delay(duration)
                        players.forEach { it.viewDistance = it.world.viewDistance }
                        sender.success("Reset viewDistance for ${if (players.size == 1) players.first().name else players.size.toString() + "players"}!")
                    }
                }
            }

        }
    }

    "simulationdistance" {
        executes(
            ArgumentTypes.players().resolve(),
            IntegerArgumentType.integer(0, 32).named("simulationDistance").default { 0 },
            DurationTypeArgument(1.seconds).named("duration").default { Duration.INFINITE }
        ) { players, simulationDistance, duration ->
            if (simulationDistance == 0 && duration == Duration.INFINITE) {
                val simulationDistances = players.groupBy { it.simulationDistance }.entries.sortedBy { it.value.size }
                if (simulationDistances.size == 1) {
                    sender.success("SimulationDistances for all players: ${simulationDistances.first().key}")
                } else sender.success(buildString {
                    append("SimulationDistances for ${simulationDistances.size} players:\n")
                    simulationDistances.forEach { (vd, players) ->
                        if (players.size > 5) append("${players.take(5).joinToString { it.name }},...: $vd\n")
                        else append("${players.joinToString { it.name }}: $vd\n")
                    }
                })
            } else {
                players.forEach {
                    it.simulationDistance = simulationDistance.takeIf { it >= 2 } ?: it.world.simulationDistance
                }
                sender.success("Successfully set ${if (players.size == 1) "<dark_green>${players.first().name}</dark_green>" else players.size.toString() + "players"} simulationDistance to $simulationDistance")
                if (duration < Duration.INFINITE) {
                    extraCommands.plugin.launch {
                        delay(duration)
                        players.forEach { it.simulationDistance = it.world.simulationDistance }
                        sender.success("Reset simulationDistance for ${if (players.size == 1) players.first().name else players.size.toString() + "players"}!")
                    }
                }
            }
        }
    }

    "sendviewdistance" {
        executes(
            ArgumentTypes.players().resolve(),
            IntegerArgumentType.integer(0, 32).named("sendViewDistance").default { 0 },
            DurationTypeArgument(1.seconds).named("duration").default { Duration.INFINITE }
        ) { players, sendViewDistance, duration ->
            extraCommands.plugin.launch {
                players.forEach {
                    it.sendViewDistance = sendViewDistance.takeIf { it >= 2 } ?: it.world.viewDistance
                }
                sender.success("Successfully set ${if (players.size == 1) "<dark_green>${players.first().name}</dark_green>" else players.size.toString() + "players"} sendViewDistance to $sendViewDistance")
                if (duration < Duration.INFINITE) {
                    delay(duration)
                    players.forEach { it.sendViewDistance = it.world.viewDistance }
                    sender.success("Reset sendViewDistance for ${if (players.size == 1) players.first().name else players.size.toString() + "players"}!")
                }
            }
        }
    }
}