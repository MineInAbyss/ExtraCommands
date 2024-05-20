package com.mineinabyss.extracommands.listeners

import com.mineinabyss.idofront.messaging.error
import net.william278.huskhomes.HuskHomes
import net.william278.huskhomes.event.ITeleportEvent
import net.william278.huskhomes.event.TeleportEvent
import net.william278.huskhomes.teleport.Target
import net.william278.huskhomes.teleport.Username
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.jvm.optionals.getOrNull

class HuskHomesListener : Listener {
    private val TELEPORT_IGNORE_BYPASS = "huskhomes.command.tpignore.bypass"
    private val huskHomes = Bukkit.getPluginManager().getPlugin("HuskHomes") as HuskHomes

    @EventHandler
    fun TeleportEvent.onTeleport() {
        val teleporter = Bukkit.getPlayer(teleport.teleporter.username) ?: return
        val onlineUser = huskHomes.getOnlineUserExact((teleport.target as? Username)?.username ?: return).getOrNull() ?: return
        val savedUser = huskHomes.getSavedUser(onlineUser).getOrNull() ?: return
        if (teleporter.hasPermission(TELEPORT_IGNORE_BYPASS) || teleporter.isOp) return

        if (savedUser.isIgnoringTeleports) isCancelled = true
        teleporter.error("${onlineUser.username} has disabled teleports...")
    }
}