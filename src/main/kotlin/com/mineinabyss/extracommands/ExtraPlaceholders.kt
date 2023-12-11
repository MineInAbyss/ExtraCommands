package com.mineinabyss.extracommands

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import kotlin.time.toKotlinDuration

class Placeholders : PlaceholderExpansion() {

    override fun getIdentifier() = "extracommands"

    override fun getAuthor() = "boy0000"

    override fun getVersion() = extraCommands.plugin.pluginMeta.version

    override fun onPlaceholderRequest(player: Player, identifier: String) =
        when (identifier) {
            "afk" -> extraCommands.config.afk.takeIf { afk -> player.idleDuration.toKotlinDuration() >= afk.idleTime }?.text ?: ""
            else -> null
        }
}