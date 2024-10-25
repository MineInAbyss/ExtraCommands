package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.info
import org.bukkit.attribute.Attribute

fun RootIdoCommands.healCommand() {
    "heal" {
        playerExecutes {
            player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            player.foodLevel = 20
            player.saturation = 20f
            player.clearActivePotionEffects()
            player.info("<green>You have been healed.")
        }
    }
}