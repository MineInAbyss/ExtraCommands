package com.mineinabyss.extracommands

import com.mineinabyss.extracommands.dailyrestarts.DailyRestartsConfig
import com.mineinabyss.idofront.serialization.DurationSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Serializable
data class ExtraConfig(
    val afk: AfkConfig = AfkConfig(),
    val dailyRestarts: DailyRestartsConfig = DailyRestartsConfig(),
) {
    @Serializable
    data class AfkConfig(
        val idleTime: @Serializable(with = DurationSerializer::class) Duration = 5.minutes,
        val text: String = ":space_-4::afk:"
    )
}