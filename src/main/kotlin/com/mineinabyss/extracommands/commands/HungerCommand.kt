package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.arguments.intArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import kotlin.math.min

fun CommandDSLEntrypoint.hungerCommand() {
    command("hunger") {
        val hunger: Int by intArg()
        playerAction {
            player.foodLevel = min(hunger, 20)
        }
    }
}
