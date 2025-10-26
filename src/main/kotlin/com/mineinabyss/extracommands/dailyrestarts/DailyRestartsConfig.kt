package com.mineinabyss.extracommands.dailyrestarts

import kotlinx.serialization.Serializable

@Serializable
data class DailyRestartsConfig(
    val enabled: Boolean = false,
    val hour: Int = 0,
    val minute: Int = 0,
    val timeZone: String? = null,
)