package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.messaging.info
import com.mojang.brigadier.StringReader
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

val vanishedPlayers = mutableSetOf<UUID>()
fun Player.toggleVanish() = (uniqueId in vanishedPlayers).also { vanished ->
    if (vanished) {
        vanishedPlayers -= uniqueId
        Bukkit.getOnlinePlayers().forEach { it.showPlayer(extraCommands.plugin, this) }
    } else {
        vanishedPlayers += uniqueId
        Bukkit.getOnlinePlayers().filter { it.uniqueId !in vanishedPlayers }.forEach {
            it.hidePlayer(extraCommands.plugin, this)
        }
        vanishedPlayers.mapNotNull(Bukkit::getPlayer).forEach { this.showPlayer(extraCommands.plugin, it) }
    }
}.not()

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