package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.commands.brigadier.playerExecutes
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success
import com.mojang.brigadier.arguments.FloatArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import org.bukkit.entity.Player

fun RootIdoCommands.movementCommands() {
    "fly" {
        playerExecutes(FloatArgumentType.floatArg(0.0f, 10.0f).default{(-1).toFloat()},) { speed ->
            when (player.allowFlight) {
                true -> {
                    player.allowFlight = false
                    player.fallDistance = 0f
                    player.error(if (sender == player) "Flight is now disabled!" else "Flight disabled for ${player.name}")
                }
                false -> {
                    player.allowFlight = true
                    if (speed != (-1).toFloat()) // Only apply fly speed change when specified by player
                        player.flySpeed = speed.div(10)
                    player.success(if (player == sender) "Flight is now enabled!" else "Flight enabled for ${player.name}")
                }
            }
        }
    }
    "flyspeed" {
        playerExecutes(FloatArgumentType.floatArg(0.0f, 10.0f),) { speed ->
            player.flySpeed = speed.div(10)
        }
    }
    "walkspeed" {
        // Divide to normalize 1.0 as default speed
        playerExecutes(FloatArgumentType.floatArg(0.0f, 10.0f),) { speed ->
            player.walkSpeed = speed.div(5)
        }
    }
    "speed" {
        playerExecutes(FloatArgumentType.floatArg(0.0f, 10.0f),) { speed ->
            when (player.isFlying) {
                true -> player.flySpeed = speed?.div(10) ?: 0.1f
                false -> player.walkSpeed = speed?.div(5) ?: 0.2f
            }
            player.success("${if (player.isFlying) "Flyspeed" else "Walkspeed"} set to ${speed ?: "default"}${if (player == sender) "" else " for ${player.name}"}")
        }
    }
}
