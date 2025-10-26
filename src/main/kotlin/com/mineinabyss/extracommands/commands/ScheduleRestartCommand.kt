package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.extracommands.dailyrestarts.RestartManager
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.arguments.DurationTypeArgument
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success
import kotlin.time.Duration.Companion.seconds

fun RootIdoCommands.scheduleRestartCommand(
    service: RestartManager = extraCommands.restartManager,
) {
    "schedulestop" {
        executes(DurationTypeArgument(10.seconds)) { duration ->
            service.scheduleStop(showTitleAtStart = true, duration)
        }
    }
    "schedulerestart" {
        executes(DurationTypeArgument(10.seconds)) { duration ->
            service.scheduleRestart(showTitleAtStart = true, duration)
        }
    }
    "cancelrestart" {
        requiresPermission("extracommands.cancelrestart")

        "daily" {
            requiresPermission("extracommands.cancelrestart.daily")
            executes {
                if (service.cancelDailyJob())
                    sender.success("Cancelled daily restart.")
                else sender.error("No daily restart scheduled, nothing to cancel.")
            }
        }

        executes {
            if (service.cancelJob())
                sender.success("Cancelled scheduled restart.")
            else sender.error("No restart scheduled, nothing to cancel.")
        }
    }
}
