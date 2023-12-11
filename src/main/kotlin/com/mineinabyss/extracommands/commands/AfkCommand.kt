package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.info
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

fun CommandDSLEntrypoint.afkCommand() {
    command("afk") {
        playerAction {
            player.info(when (player.toggleAfk()) {
                true -> "<gray>You are now AFK"
                false -> "<gray>You are no longer AFK"
            })
        }
    }
}