package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.ensureSenderIsPlayer
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import org.bukkit.GameMode
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

fun RootIdoCommands.suicideCommand() {
    ("suicide" / "kms") {
        playerExecutes {
            val player = sender as Player
            val event = EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, DamageSource.builder(DamageType.GENERIC_KILL).build(), Double.MAX_VALUE)
            event.callEvent()
            event.entity.lastDamageCause = event
            player.health = 0.0
        }
    }
}