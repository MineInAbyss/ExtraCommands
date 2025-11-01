package com.mineinabyss.extracommands.dailyrestarts

import com.mineinabyss.idofront.serialization.DurationSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

@Serializable
data class DailyRestartsConfig(
    val enabled: Boolean = false,
    val skipIfUptimeLessThan: @Serializable(with = DurationSerializer::class) Duration = 4.hours,
    val hour: Int = 0,
    val minute: Int = 0,
    val timeZone: String? = null,
)