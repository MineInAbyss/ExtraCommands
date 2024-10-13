package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.info
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

val vanishedPlayers = mutableSetOf<UUID>()
fun Player.toggleVanish() = when (uniqueId in vanishedPlayers) {
    true -> {
        vanishedPlayers -= uniqueId
        Bukkit.getOnlinePlayers().forEach { it.showPlayer(extraCommands.plugin, this) }
        false
    }
    false -> {
        vanishedPlayers += uniqueId
        Bukkit.getOnlinePlayers().forEach { it.hidePlayer(extraCommands.plugin, this) }
        true
    }
}

fun RootIdoCommands.vanishCommand() {
    "vanish" {
        requiresPermission("extracommands.vanish")
        playerExecutes {
            player.info(when (player.toggleVanish()) {
                true -> "<gray>You are now Vanished"
                false -> "<gray>You are no longer Vanished"
            })
        }
    }
}