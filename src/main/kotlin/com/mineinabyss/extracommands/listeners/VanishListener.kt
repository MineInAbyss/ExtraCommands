package com.mineinabyss.extracommands.listeners

import com.mineinabyss.extracommands.commands.vanishedPlayers
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.brigadier.IdoCommand.Companion.hasPermissionRecursive
import com.mineinabyss.idofront.entities.toPlayer
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class VanishListener : Listener {

    private val bypassPermission = "extracommands.vanish.bypass"

    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        Bukkit.getOnlinePlayers().forEach {
            if (it.uniqueId !in vanishedPlayers && !it.hasPermission(bypassPermission)) return@forEach
            it.sendMessage(joinMessage()!!)
        }
        vanishedPlayers.mapNotNull(Bukkit::getPlayer).forEach { player.hidePlayer(extraCommands.plugin, it) }
    }

    @EventHandler
    fun PlayerQuitEvent.onQuit() {
        if (player.uniqueId !in vanishedPlayers || quitMessage() == null) return
        Bukkit.getOnlinePlayers().forEach {
            if (it.uniqueId !in vanishedPlayers && !it.hasPermission(bypassPermission)) return@forEach
            it.sendMessage(quitMessage()!!)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun PlayerDeathEvent.onDeath() {
        if (player.uniqueId !in vanishedPlayers || deathMessage() == null) return
        Bukkit.getOnlinePlayers().forEach {
            if (it.uniqueId !in vanishedPlayers && !it.hasPermission(bypassPermission)) return@forEach
            it.sendMessage(deathMessage()!!)
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun AsyncChatEvent.onAsyncChat() {
        if (player.uniqueId !in vanishedPlayers) return
        viewers().removeIf { it is Player && it.uniqueId !in vanishedPlayers && !it.hasPermission(bypassPermission) }
    }
}