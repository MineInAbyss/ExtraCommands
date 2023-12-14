package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.info
import kotlin.time.Duration

private val serverStartTime = System.currentTimeMillis()
fun CommandDSLEntrypoint.uptimeCommand() {
    command("uptime") {
        playerAction {
            val uptime = calculateTime(System.currentTimeMillis() - serverStartTime)
            player.info("<gold>Server has been up for: <yellow>$uptime")
        }
    }
}