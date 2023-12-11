package com.mineinabyss.extracommands.commands

import com.mineinabyss.geary.papermc.tracking.entities.toGeary
import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.info
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.GameMode
import org.bukkit.entity.Player

@Serializable @SerialName("extracommands:afk") class AfkPlayer

fun Player.toggleAfk() = toGeary().let {
    if (it.has<AfkPlayer>()) it.remove<AfkPlayer>()
    else it.set(AfkPlayer())
    it.has<AfkPlayer>()
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