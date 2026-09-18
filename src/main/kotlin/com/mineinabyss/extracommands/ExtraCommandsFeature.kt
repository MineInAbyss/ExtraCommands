package com.mineinabyss.extracommands

import com.mineinabyss.dependencies.module
import com.mineinabyss.extracommands.listeners.AfkListener
import com.mineinabyss.extracommands.listeners.GodListener
import com.mineinabyss.extracommands.listeners.HuskHomesListener
import com.mineinabyss.extracommands.listeners.SeenListener
import com.mineinabyss.extracommands.listeners.VanishListener
import com.mineinabyss.idofront.features.listeners
import com.mineinabyss.idofront.plugin.Plugins

val ExtraCommandsFeature = module("extra-commands") {
    listeners(
        AfkListener(),
        GodListener(),
        SeenListener(),
        VanishListener(),
    )

    if (Plugins.isEnabled("HuskHomes")) listeners(HuskHomesListener())

    if (Plugins.isEnabled("PlaceholderAPI")) ExtraPlaceholders().register()
}
