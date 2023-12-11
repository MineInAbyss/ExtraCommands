package com.mineinabyss.extracommands

import com.mineinabyss.idofront.di.DI
import org.bukkit.plugin.java.JavaPlugin

class ExtraCommands : JavaPlugin() {

    override fun onEnable() {
        createExtraCommandsContext()
        ExtraCommandExecutor()

        ExtraPlaceholders().register()
    }

    fun createExtraCommandsContext() {
        DI.remove<ExtraCommandContext>()
        DI.add<ExtraCommandContext>(object : ExtraCommandContext {
            override val plugin = this@ExtraCommands
            override val config = ExtraConfig()
        })
    }
}
