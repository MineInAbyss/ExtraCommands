package com.mineinabyss.extracommands

import com.mineinabyss.dependencies.DI
import com.mineinabyss.extracommands.dailyrestarts.RestartManager
import org.bukkit.plugin.Plugin

/**
 * Easy access to information related to the [ExtraCommands] plugin.
 */
interface ExtraCommandContext : Plugin, DI {
    val plugin: Plugin
    val config: ExtraConfig
    val restartManager: RestartManager

    companion object {
        var instance: ExtraCommandContext? = null
    }
}

val extraCommands get() = ExtraCommandContext.instance ?: error("ExtraCommands not loaded yet!")
