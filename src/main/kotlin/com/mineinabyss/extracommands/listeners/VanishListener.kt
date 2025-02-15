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
        val joinMsg = joinMessage() ?: return
        if (player.uniqueId in vanishedPlayers) joinMessage(null)
        Bukkit.getOnlinePlayers().forEach {
            if (it.uniqueId !in vanishedPlayers && !it.hasPermission(bypassPermission)) return@forEach
            it.sendMessage(joinMsg)
        }
        vanishedPlayers.mapNotNull(Bukkit::getPlayer).forEach { player.hidePlayer(extraCommands.plugin, it) }
    }

    @EventHandler
    fun PlayerQuitEvent.onQuit() {
        val quitMsg = quitMessage() ?: return
        if (player.uniqueId !in vanishedPlayers) return
        quitMessage(null)
        Bukkit.getOnlinePlayers().forEach {
            if (it.uniqueId !in vanishedPlayers && !it.hasPermission(bypassPermission)) return@forEach
            it.sendMessage(quitMsg)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun PlayerDeathEvent.onDeath() {
        val deathMsg = deathMessage() ?: return
        if (player.uniqueId !in vanishedPlayers) return
        deathMessage(null)
        Bukkit.getOnlinePlayers().forEach {
            if (it.uniqueId !in vanishedPlayers && !it.hasPermission(bypassPermission)) return@forEach
            it.sendMessage(deathMsg)
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun AsyncChatEvent.onAsyncChat() {
        if (player.uniqueId !in vanishedPlayers) return
        viewers().removeIf { it is Player && it.uniqueId !in vanishedPlayers && !it.hasPermission(bypassPermission) }
    }
}