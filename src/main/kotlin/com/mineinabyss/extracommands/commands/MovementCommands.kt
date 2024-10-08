package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success
import com.mojang.brigadier.arguments.FloatArgumentType

fun RootIdoCommands.movementCommands() {
    "fly" {
        requiresPermission("extracommands.fly")
        playerExecutes {
            when (player.allowFlight) {
                true -> {
                    player.allowFlight = false
                    player.fallDistance = 0f
                    player.error(if (sender == player) "Flight is now disabled!" else "Flight disabled for ${player.name}")
                }
                false -> {
                    player.allowFlight = true
                    player.success(if (player == sender) "Flight is now enabled!" else "Flight enabled for ${player.name}")
                }
            }
        }
    }
    "flyspeed" {
        requiresPermission("extracommands.flyspeed")
        val speed by FloatArgumentType.floatArg(0.0f, 10.0f)
        playerExecutes {
            player.flySpeed = speed().div(10)
        }
    }
    "walkspeed" {
        requiresPermission("extracommands.walkspeed")
        // Divide to normalize 1.0 as default speed
        val speed by FloatArgumentType.floatArg(0.0f, 10.0f)
        playerExecutes {
            player.walkSpeed = speed().div(5)
        }
    }
    "speed" {
        requiresPermission("extracommands.speed")
        val speed by FloatArgumentType.floatArg(0.0f, 10.0f)
        playerExecutes {
            when (player.isFlying) {
                true -> player.flySpeed = speed()?.div(10) ?: 0.1f
                false -> player.walkSpeed = speed()?.div(5) ?: 0.2f
            }
            player.success("${if (player.isFlying) "Flyspeed" else "Walkspeed"} set to ${speed() ?: "default"}${if (player == sender) "" else " for ${player.name}"}")
        }
    }
}
