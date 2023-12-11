package com.mineinabyss.extracommands

import com.mineinabyss.extracommands.commands.AfkPlayer
import com.mineinabyss.geary.papermc.tracking.entities.toGeary
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import kotlin.time.toKotlinDuration

class ExtraPlaceholders : PlaceholderExpansion() {

    override fun getIdentifier() = "extracommands"

    override fun getAuthor() = "boy0000"

    override fun getVersion() = extraCommands.plugin.pluginMeta.version

    override fun onPlaceholderRequest(player: Player, identifier: String) =
        when (identifier) {
            "afk" -> extraCommands.config.afk.takeIf { afk -> player.idleDuration.toKotlinDuration() >= afk.idleTime || player.toGeary().has<AfkPlayer>() }?.text ?: ""
            else -> null
        }
}