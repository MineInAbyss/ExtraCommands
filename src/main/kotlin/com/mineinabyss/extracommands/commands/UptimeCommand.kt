package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.info

private val serverStartTime = System.currentTimeMillis()
fun RootIdoCommands.uptimeCommand() {
    "uptime" {
        executes {
            val uptime = calculateTime(System.currentTimeMillis() - serverStartTime)
            sender.info("<gold>Server has been up for: <yellow>$uptime")
        }
    }
}