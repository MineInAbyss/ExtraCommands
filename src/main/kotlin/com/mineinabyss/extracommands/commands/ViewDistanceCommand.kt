package com.mineinabyss.extracommands.commands

import com.github.shynixn.mccoroutine.bukkit.launch
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.arguments.DurationTypeArgument
import com.mineinabyss.idofront.messaging.success
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.sun.jdi.connect.Connector.IntegerArgument
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import kotlinx.coroutines.delay
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun RootIdoCommands.viewDistanceCommands() {

    fun handleViewDistance(sender: CommandSender, players: List<Player>, distance: Int, duration: Duration?) {
        extraCommands.plugin.launch {
            players.forEach { it.viewDistance = distance.takeUnless { it == -1 } ?: it.world.viewDistance }
            sender.success("Successfully set ${if (players.size == 1) "<dark_green>${players.first().name}</dark_green>" else players.size.toString() + "players"} viewDistance to <dark_green><i>$distance")
            if (duration != null) {
                delay(duration)
                players.forEach { it.viewDistance = it.world.viewDistance }
                sender.success("Reset viewDistance for ${if (players.size == 1) players.first().name else players.size.toString() + "players"}!")
            }
        }
    }
    "viewdistance" {
        val players by ArgumentTypes.players()
        executes {
            handleViewDistance(sender, players(), -1, null)
        }
        val viewDistance by IntegerArgumentType.integer(2, 32)
        executes {
            handleViewDistance(sender, players(), viewDistance(), null)
        }
        val duration by DurationTypeArgument(1.seconds)
        executes {
            handleViewDistance(sender, players(), viewDistance(), duration())
        }
    }

    fun handleSimulationDistance(sender: CommandSender, players: List<Player>, distance: Int, duration: Duration?) {
        extraCommands.plugin.launch {
            players.forEach { it.simulationDistance = distance.takeUnless { it == -1 } ?: it.world.simulationDistance }
            sender.success("Successfully set ${if (players.size == 1) "<dark_green>${players.first().name}</dark_green>" else players.size.toString() + "players"} simulationDistance to $distance")
            if (duration != null) {
                delay(duration)
                players.forEach { it.simulationDistance = it.world.simulationDistance }
                sender.success("Reset simulationDistance for ${if (players.size == 1) players.first().name else players.size.toString() + "players"}!")
            }
        }
    }
    "simulationdistance" {
        val players by ArgumentTypes.players()
        executes {
            handleSimulationDistance(sender, players(), -1, null)
        }
        val simulationDistance by IntegerArgumentType.integer(2, 32)
        executes {
            handleSimulationDistance(sender, players(), simulationDistance(), null)
        }
        val duration by DurationTypeArgument(1.seconds)
        executes {
            handleSimulationDistance(sender, players(), simulationDistance(), duration())
        }
    }

    fun handleSendViewDistance(sender: CommandSender, players: List<Player>, distance: Int, duration: Duration?) {
        extraCommands.plugin.launch {
            players.forEach { it.sendViewDistance = distance.takeUnless { it == -1 } ?: it.world.viewDistance }
            sender.success("Successfully set ${if (players.size == 1) "<dark_green>${players.first().name}</dark_green>" else players.size.toString() + "players"} sendViewDistance to $distance")
            if (duration != null) {
                delay(duration)
                players.forEach { it.sendViewDistance = it.world.viewDistance }
                sender.success("Reset sendViewDistance for ${if (players.size == 1) players.first().name else players.size.toString() + "players"}!")
            }
        }
    }

    "sendviewdistance" {
        val players by ArgumentTypes.players()
        executes {
            handleSendViewDistance(sender, players(), -1, null)
        }
        val sendViewDistance by IntegerArgumentType.integer(2, 32)
        executes {
            handleSendViewDistance(sender, players(), sendViewDistance(), null)
        }
        val duration by DurationTypeArgument(1.seconds)
        executes {
            handleSendViewDistance(sender, players(), sendViewDistance(), duration())
        }
    }
}