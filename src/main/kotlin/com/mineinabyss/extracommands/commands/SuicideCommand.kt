package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.ensureSenderIsPlayer
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

fun CommandDSLEntrypoint.suicideCommand() {
    command("suicide", "kms") {
        ensureSenderIsPlayer()
        action {
            val player = sender as Player
            val event = EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, Double.MAX_VALUE)
            event.callEvent()
            event.entity.lastDamageCause = event
            player.health = 0.0

        }
    }
}