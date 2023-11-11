package com.mineinabyss.extracommands

import com.mineinabyss.idofront.di.DI

val extraCommands by DI.observe<ExtraCommandContext>()
interface ExtraCommandContext {
    val plugin: ExtraCommands
}
