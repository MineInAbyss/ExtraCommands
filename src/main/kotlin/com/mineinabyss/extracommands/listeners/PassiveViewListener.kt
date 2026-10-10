package com.mineinabyss.extracommands.listeners

import com.lishid.openinv.event.OpenPlayerSaveEvent
import com.lishid.openinv.event.PlayerSaveEvent
import com.lishid.openinv.util.Permissions
import com.mineinabyss.extracommands.commands.isPassiveEditBlocked
import com.mineinabyss.extracommands.commands.passiveViewOnly
import com.mineinabyss.extracommands.commands.persistCancelled
import com.mineinabyss.extracommands.commands.persistingPlayer
import com.mineinabyss.geary.papermc.tracking.items.passive.PassiveInventory
import com.mineinabyss.geary.papermc.tracking.items.passive.PassiveSlots
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent

class PassiveViewListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun InventoryClickEvent.blockViewOnly() {
        val passive = view.topInventory.holder as? PassiveInventory ?: return
        if (isPassiveEditBlocked(view, passive)) isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun InventoryDragEvent.blockViewOnly() {
        val passive = view.topInventory.holder as? PassiveInventory ?: return
        if (isPassiveEditBlocked(view, passive)) isCancelled = true
    }

    // LOWEST keeps Geary's rules from running, these undo any plugin uncancelling in between
    @EventHandler(priority = EventPriority.HIGHEST)
    fun InventoryClickEvent.reblockViewOnly() = blockViewOnly()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun InventoryDragEvent.reblockViewOnly() = blockViewOnly()

    @EventHandler
    fun InventoryCloseEvent.forgetViewOnly() {
        passiveViewOnly -= view
    }

    // A detached inventory becomes the live one on join, so viewers only allowed offline access lose it
    @EventHandler
    fun PlayerJoinEvent.closeOfflineOnlyViewers() = closeViewersWithoutAccess(player)

    @EventHandler
    fun PlayerChangedWorldEvent.closeCrossWorldViewers() {
        closeViewersWithoutAccess(player)
        val viewed = (player.openInventory.topInventory.holder as? PassiveInventory)?.player ?: return
        if (viewed != player && viewed.world != player.world && !Permissions.ACCESS_CROSSWORLD.hasPermission(player)) player.closeInventory()
    }

    private fun closeViewersWithoutAccess(owner: Player) {
        val passive = PassiveSlots.get(owner) ?: return
        passive.inventory.viewers.toList()
            .filter { it.uniqueId != owner.uniqueId }
            .filter { !Permissions.ACCESS_ONLINE.hasPermission(it) || (it.world != owner.world && !Permissions.ACCESS_CROSSWORLD.hasPermission(it)) }
            .forEach { it.closeInventory() }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun PlayerSaveEvent.trackPersistCancel() {
        if (isCancelled && player.uniqueId == persistingPlayer) persistCancelled = true
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun OpenPlayerSaveEvent.trackPersistCancel() {
        if (isCancelled && player.uniqueId == persistingPlayer) persistCancelled = true
    }
}
