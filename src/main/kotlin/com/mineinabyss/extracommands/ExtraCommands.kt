package com.mineinabyss.extracommands

import com.mineinabyss.dependencies.DI
import com.mineinabyss.dependencies.DIContext
import com.mineinabyss.dependencies.get
import com.mineinabyss.dependencies.getLazy
import com.mineinabyss.dependencies.loadCatching
import com.mineinabyss.dependencies.scope
import com.mineinabyss.dependencies.single
import com.mineinabyss.extracommands.dailyrestarts.RestartManager
import com.mineinabyss.idofront.config.SingleConfig
import com.mineinabyss.idofront.features.singleConfig
import com.mineinabyss.idofront.features.singlePluginLogger
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class ExtraCommands : JavaPlugin(), ExtraCommandContext {
    override val di: DIContext = DI {
        single<Plugin> { this@ExtraCommands }
        singlePluginLogger(this@ExtraCommands)
        singleConfig<ExtraConfig>("config.yml") { default = ExtraConfig() }
        single { RestartManager(get<ExtraConfig>().dailyRestarts) }
    }

    override val plugin: Plugin get() = this
    override val config: ExtraConfig by getLazy()
    override val restartManager: RestartManager by getLazy()

    override fun onLoad() {
        ExtraCommandContext.instance = this@ExtraCommands
    }

    override fun onEnable() {
        scope.loadCatching(ExtraCommandsFeature)

        ExtraBrigadierCommands.registerCommands()
        restartManager.scheduleDailyRestartIfEnabled()
    }

    override fun reloadConfig() {
        get<SingleConfig<ExtraConfig>>().updateCached()
    }
}
