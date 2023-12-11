package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.arguments.genericArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success

fun CommandDSLEntrypoint.movementCommands() {
    command("flyspeed") {
        val speed: Double by genericArg(parseFunction = { it.takeIf { it != "reset" && (it.toDouble() in 0.0..1.0) }?.toDoubleOrNull() ?: 0.1 })
        playerAction {
            player.flySpeed = speed.toFloat()
        }
    }
    command("fly") {
        playerAction {
            when (player.allowFlight) {
                true -> {
                    player.allowFlight = false
                    player.fallDistance = 0f
                    player.flySpeed = 0.1f
                    player.error(if (sender == player) "Flight is now disabled!" else "Flight disabled for ${player.name}")
                }
                false -> {
                    val defaultSpeed = 1.0f
                    player.allowFlight = true
                    player.flySpeed = defaultSpeed
                    player.success(if (player == sender) "Flight is now enabled!" else "Flight enabled for ${player.name}")
                }
            }
            player.allowFlight = !player.allowFlight
            player.flySpeed = 0.1f
        }
    }
    command("walkspeed") {
        val speed: Double by genericArg(parseFunction = { it.takeIf { it != "reset" && (it.toDouble() in 0.0..1.0) }?.toDoubleOrNull() ?: 0.2 })
        playerAction {
            player.walkSpeed = speed.toFloat()
        }
    }
    command("speed") {
        val speed: Double? by genericArg(parseFunction = { it.takeIf { it != "reset" && (it.toDouble() in 0.0..1.0) }?.toDoubleOrNull() })
        playerAction {
            when (player.isFlying) {
                true -> player.flySpeed = speed?.toFloat() ?: 0.1f
                false -> player.walkSpeed = speed?.toFloat() ?: 0.2f
            }
            player.success("${if (player.isFlying) "Flyspeed" else "Walkspeed"} set to $speed${if (player == sender) "" else " for ${player.name}"}")
        }
    }
}
