package com.mineinabyss.extracommands

import com.mineinabyss.extracommands.listeners.AfkListener
import com.mineinabyss.extracommands.listeners.GodListener
import com.mineinabyss.extracommands.listeners.HuskHomesListener
import com.mineinabyss.extracommands.listeners.SeenListener
import com.mineinabyss.extracommands.listeners.VanishListener
import com.mineinabyss.idofront.di.DI
import com.mineinabyss.idofront.plugin.Plugins
import com.mineinabyss.idofront.plugin.listeners
import org.bukkit.plugin.java.JavaPlugin

class ExtraCommands : JavaPlugin() {

    override fun onEnable() {
        createExtraCommandsContext()
        ExtraBrigadierCommands.registerCommands()

        listeners(
            AfkListener(),
            GodListener(),
            SeenListener(),
            VanishListener()
        )

        if (Plugins.isEnabled("HuskHomes")) listeners(HuskHomesListener())

        if (Plugins.isEnabled("PlaceholderAPI")) ExtraPlaceholders().register()
    }

    fun createExtraCommandsContext() {
        DI.remove<ExtraCommandContext>()
        DI.add<ExtraCommandContext>(object : ExtraCommandContext {
            override val plugin = this@ExtraCommands
            override val config = ExtraConfig()
        })
    }
}
