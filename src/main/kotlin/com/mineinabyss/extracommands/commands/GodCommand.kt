package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.success

fun RootIdoCommands.godCommand() {
    "god" {
        playerExecutes {
            player.isInvulnerable = !player.isInvulnerable
            player.success("God mode is now ${if (player.isInvulnerable) "on" else "off"} ${if (sender == player) "" else "for ${player.name}"}")
        }
    }
}
