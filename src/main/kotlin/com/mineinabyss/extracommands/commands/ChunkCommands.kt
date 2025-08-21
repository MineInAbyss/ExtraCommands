package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.executes
import com.mineinabyss.idofront.messaging.info
import com.mojang.brigadier.arguments.IntegerArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun RootIdoCommands.chunkCommands() {
    "chunkInfo" {
        executes(
            IntegerArgumentType.integer().named("chunkX"),
            IntegerArgumentType.integer().named("chunkZ"),
            ArgumentTypes.world().named("world").default { (sender as? Player)?.world ?: Bukkit.getWorlds().first() },
        ) { chunkX, chunkZ, world ->
            val entitiesByType = world.entities.filter { it.chunk.x == chunkX && it.chunk.z == chunkZ }.groupBy { it.type }
                .mapValues { it.value.partition { it.isTicking } }
            sender.info("""
                Chunk ($chunkX, $chunkZ):
                Loaded: ${world.isChunkLoaded(chunkX, chunkZ)}
                Entities:
                ${entitiesByType.entries.joinToString("\n") { "${it.key.name} | Ticking: ${it.value.first.count()} | Non-Ticking: ${it.value.second.count()}"  }}
            """.trimIndent())
        }
    }
}