package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.messaging.info
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import org.bukkit.entity.Player
import java.util.*

val afkPlayers = mutableSetOf<UUID>()
fun Player.toggleAfk() = when (uniqueId in afkPlayers) {
    true -> {
        afkPlayers -= uniqueId
        false
    }
    false -> {
        afkPlayers += uniqueId
        true
    }
}

fun RootIdoCommands.afkCommand() {
    "afk" {
        playerExecutes {
            player.info(when (player.toggleAfk()) {
                true -> "<gray>You are now AFK"
                false -> "<gray>You are no longer AFK"
            })
        }
    }
}