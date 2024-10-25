package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.info

private val serverStartTime = System.currentTimeMillis()
fun RootIdoCommands.uptimeCommand() {
    "uptime" {
        playerExecutes {
            val uptime = calculateTime(System.currentTimeMillis() - serverStartTime)
            player.info("<gold>Server has been up for: <yellow>$uptime")
        }
    }
}