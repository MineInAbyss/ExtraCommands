package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

fun RootIdoCommands.suicideCommand() {
    ("suicide" / "kms") {
        playerExecutes {
            val event = EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, DamageSource.builder(DamageType.GENERIC_KILL).build(), Double.MAX_VALUE)
            event.callEvent()
            event.entity.lastDamageCause = event
            player.health = 0.0
        }
    }
}