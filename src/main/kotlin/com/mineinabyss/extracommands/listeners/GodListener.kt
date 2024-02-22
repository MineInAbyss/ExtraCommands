package com.mineinabyss.extracommands.listeners

import com.mineinabyss.idofront.messaging.broadcast
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class GodListener : Listener {

    @EventHandler
    fun EntityDamageEvent.onDamage() {
        if (!entity.isInvulnerable) return
        isCancelled = true
        entity.fallDistance = 0f
    }
}