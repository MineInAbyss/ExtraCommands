package com.mineinabyss.extracommands.commands

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.lishid.openinv.IOpenInv
import com.lishid.openinv.util.Permissions
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.geary.papermc.tracking.items.passive.PassiveInventory
import com.mineinabyss.geary.papermc.tracking.items.passive.PassiveSlots
import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.entities.toOfflinePlayer
import com.mineinabyss.idofront.messaging.error
import com.mojang.brigadier.arguments.StringArgumentType
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryView
import org.bukkit.plugin.Plugin
import java.util.Collections
import java.util.UUID
import java.util.WeakHashMap

const val PASSIVE_EDIT_PERMISSION = "extracommands.openpassive.edit"

// Keyed by view, opening a new view closes the previous one and would clear a per-player flag mid-open
val passiveViewOnly: MutableSet<InventoryView> = Collections.newSetFromMap(WeakHashMap())
val openInv get() = Bukkit.getPluginManager().getPlugin("OpenInv") as IOpenInv

fun RootIdoCommands.openPassiveCommand() {
    ("openpassive" / "passiveinv") {
        permission = "extracommands.openpassive"
        executes.asPlayer().args("player" to StringArgumentType.word().suggests {
            suggestFiltering(Bukkit.getOnlinePlayers().map { it.name })
        }) { name ->
            val viewer = player
            val plugin = extraCommands.plugin
            plugin.launch(plugin.asyncDispatcher) {
                val target = openInv.matchPlayer(name)
                withContext(plugin.minecraftDispatcher) {
                    if (!viewer.isOnline) return@withContext
                    // The lookup may return an online player object that went stale if they quit meanwhile
                    val current = target?.uniqueId?.toOfflinePlayer()?.takeUnless { !it.hasPlayedBefore() && !it.isOnline }
                        ?: return@withContext viewer.error("No player named $name has joined the server")

                    openPassive(viewer, current)
                }
            }
        }
    }
}

private fun openPassive(viewer: Player, target: OfflinePlayer) {
    val self = target.uniqueId == viewer.uniqueId
    val online = target.isOnline
    if (!self && !(if (online) Permissions.ACCESS_ONLINE else Permissions.ACCESS_OFFLINE).hasPermission(viewer))
        return viewer.error("You do not have permission to open the passive slots of ${if (online) "online" else "offline"} players")
    if (!online && openInv.disableOfflineAccess()) return viewer.error("Offline access is disabled in OpenInv")

    val targetPlayer = (if (online) target.player else openInv.loadPlayer(target))
        ?: return viewer.error("Could not load ${target.name}")
    val equalAccessViewOnly = if (self) false else accessViewOnly(viewer, targetPlayer) ?: return

    val passive = (if (online) PassiveSlots.get(targetPlayer) else PassiveSlots.inventoryFor(targetPlayer) { write -> persistOffline(target.uniqueId, write) })
        ?: return viewer.error("Could not load the passive slots of ${target.name}")

    val viewOnly = !self && (equalAccessViewOnly || !viewer.hasPermission(PASSIVE_EDIT_PERMISSION) || (!online && openInv.disableSaving()))
    val view = viewer.openInventory(passive.inventory) ?: return PassiveSlots.releaseIfUnused(passive)
    if (viewOnly) passiveViewOnly += view
}

private fun accessViewOnly(viewer: Player, target: Player): Boolean? {
    if (!Permissions.ACCESS_CROSSWORLD.hasPermission(viewer) && target.world != viewer.world) {
        viewer.error("${target.name} is in another world")
        return null
    }
    for (level in 4 downTo 1) {
        val permission = "openinv.access.level.$level"
        if (!target.hasPermission(permission)) {
            if (viewer.hasPermission(permission)) return false
            continue
        }
        val equalAccess = when {
            Permissions.ACCESS_EQUAL_EDIT.hasPermission(viewer) -> false
            Permissions.ACCESS_EQUAL_VIEW.hasPermission(viewer) -> true
            Permissions.ACCESS_EQUAL_DENY.hasPermission(viewer) -> null
            // Mirrors AccessEqualMode.of, which lives outside OpenInv's API
            else -> when ((openInv as Plugin).config.getString("settings.equal-access")?.lowercase()) {
                "deny", "false" -> null
                "allow", "true" -> false
                else -> true
            }
        }
        if (!viewer.hasPermission(permission) || equalAccess == null) {
            viewer.error("${target.name} is exempt from being viewed")
            return null
        }
        return equalAccess
    }
    return false
}

private fun persistOffline(playerId: UUID, write: (Player) -> Unit) {
    check(!openInv.disableSaving()) { "OpenInv saving is disabled" }
    val loaded = openInv.loadPlayer(playerId.toOfflinePlayer()) ?: error("Could not load player data of $playerId")
    val current = (loaded as CraftPlayer).handle.bukkitEntity
    write(current)
    persistingPlayer = playerId
    persistCancelled = false
    try {
        current.saveData()
    } finally {
        persistingPlayer = null
    }
    check(!persistCancelled) { "Saving $playerId was cancelled by another plugin" }
}

var persistingPlayer: UUID? = null
var persistCancelled = false

fun isPassiveEditBlocked(view: InventoryView, passive: PassiveInventory) =
    view in passiveViewOnly || (PassiveSlots.isDetached(passive) && (openInv.disableSaving() || openInv.disableOfflineAccess()))

fun closePassiveViews() {
    Bukkit.getOnlinePlayers().filter { it.openInventory.topInventory.holder is PassiveInventory }.forEach { it.closeInventory() }
}
