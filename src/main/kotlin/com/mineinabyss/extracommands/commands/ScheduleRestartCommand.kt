package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.extracommands.dailyrestarts.RestartManager
import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.commands.brigadier.arguments.DurationTypeArgument
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success
import kotlin.time.Duration.Companion.seconds

fun RootIdoCommands.scheduleRestartCommand(
    service: RestartManager = extraCommands.restartManager,
) {
    "schedulestop" {
        executes.args("duration" to DurationTypeArgument(10.seconds)) { duration ->
            service.scheduleStop(showTitleAtStart = true, duration)
        }
    }
    "schedulerestart" {
        executes.args("duration" to DurationTypeArgument(10.seconds)) { duration ->
            service.scheduleRestart(showTitleAtStart = true, duration)
        }
    }
    "cancelrestart" {
        permission = "extracommands.cancelrestart"

        "daily" {
            permission = "extracommands.cancelrestart.daily"
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
