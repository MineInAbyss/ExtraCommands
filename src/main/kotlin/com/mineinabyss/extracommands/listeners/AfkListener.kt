package com.mineinabyss.extracommands.listeners

import com.mineinabyss.extracommands.commands.afkPlayers
import com.mineinabyss.idofront.messaging.info
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent

class AfkListener : Listener {

    @EventHandler
    fun PlayerMoveEvent.onMove() {
        if (hasChangedPosition() && afkPlayers.remove(player.uniqueId))
            player.info("<gray>You are no longer AFK")
    }

    @EventHandler
    fun PlayerQuitEvent.onQuit() {
        afkPlayers.remove(player.uniqueId)
    }

    @EventHandler
    fun AsyncChatEvent.onChat() {
        if (afkPlayers.remove(player.uniqueId))
            player.info("<gray>You are no longer AFK")
    }
}