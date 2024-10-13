package com.mineinabyss.extracommands.listeners

import com.mineinabyss.extracommands.commands.vanishedPlayers
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.entities.toPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class VanishListener : Listener {

    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        vanishedPlayers.mapNotNull { it.toPlayer() }.forEach { player.hidePlayer(extraCommands.plugin, it) }
    }

    @EventHandler
    fun PlayerQuitEvent.onQuit() {
        vanishedPlayers -= player.uniqueId
    }
}