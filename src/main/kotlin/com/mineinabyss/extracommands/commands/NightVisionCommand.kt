package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.messaging.info
import org.bukkit.potion.PotionEffectType

fun RootIdoCommands.nightVisionCommand() {
    ("nightvision" / "nv") {
        executes.asPlayer {
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION)
                player.info("<green>Night vision disabled.")
            } else {
                player.addPotionEffect(
                    PotionEffectType.NIGHT_VISION.createEffect(Int.MAX_VALUE, 0).withParticles(false)
                )
                player.info("<green>Night vision enabled.")
            }
        }
    }
}
