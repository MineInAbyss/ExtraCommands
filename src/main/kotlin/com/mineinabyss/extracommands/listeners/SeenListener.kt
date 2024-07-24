package com.mineinabyss.extracommands.listeners

import kotlinx.coroutines.Job
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class SeenListener : Listener {

    @EventHandler
    fun PlayerJoinEvent.onPlayerJoin() {
        previouslyOnline -= player.name
        currentlyQuerying[player]?.cancel()
    }

    @EventHandler
    fun PlayerQuitEvent.onPlayerQuit() {
        previouslyOnline += player.name
        currentlyQuerying[player]?.cancel()
    }

    companion object {
        val previouslyOnline = mutableSetOf<String>()
        val currentlyQuerying = mutableMapOf<CommandSender, Job>()
    }
}