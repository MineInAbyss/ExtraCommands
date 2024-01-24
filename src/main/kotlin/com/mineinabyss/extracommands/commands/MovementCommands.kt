package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.arguments.genericArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success

fun CommandDSLEntrypoint.movementCommands() {
    command("fly") {
        playerAction {
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
    command("flyspeed") {
        val speed: Double by genericArg(parseFunction = { it.toDoubleOrNull()?.div(10)?.coerceIn(0.0, 1.0) ?: 0.1 })
        playerAction {
            player.flySpeed = speed.toFloat()
        }
    }
    command("walkspeed") {
        // Divide to normalize 1.0 as default speed
        val speed: Double by genericArg(parseFunction = { it.toDoubleOrNull()?.div(5)?.coerceIn(0.0, 1.0) ?: 0.2 })
        playerAction {
            player.walkSpeed = speed.toFloat()
        }
    }
    command("speed") {
        val speed: Double? by genericArg(parseFunction = { it.toDoubleOrNull() })
        playerAction {
            when (player.isFlying) {
                true -> player.flySpeed = speed?.div(10)?.coerceIn(0.0, 1.0)?.toFloat() ?: 0.1f
                false -> player.walkSpeed = speed?.div(5)?.coerceIn(0.0, 1.0)?.toFloat() ?: 0.2f
            }
            player.success("${if (player.isFlying) "Flyspeed" else "Walkspeed"} set to ${speed ?: "default"}${if (player == sender) "" else " for ${player.name}"}")
        }
    }
}
