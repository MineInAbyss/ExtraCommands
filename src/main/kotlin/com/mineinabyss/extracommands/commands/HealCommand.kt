package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.messaging.info
import org.bukkit.attribute.Attribute

fun RootIdoCommands.healCommand() {
    "heal" {
        executes.asPlayer {
            player.health = player.getAttribute(Attribute.MAX_HEALTH)!!.value
            player.foodLevel = 20
            player.saturation = 20f
            player.clearActivePotionEffects()
            player.info("<green>You have been healed.")
        }
    }
}