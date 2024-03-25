package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extensions.DeletedMapRenderer
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.commands.arguments.intArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.logSuccess
import com.mineinabyss.idofront.textcomponents.miniMsg

fun CommandDSLEntrypoint.mapCommands() {
    command("maps") {
        "delete" {
            val mapId by intArg()
            action {
                extraCommands.plugin.server.getMap(mapId)?.let { mapView ->
                    mapView.renderers.forEach(mapView::removeRenderer)
                    val deleted = DeletedMapRenderer()
                    if (!deleted.loadImage()) return@action sender.error("Failed to load Blocked Image")

                    mapView.addRenderer(deleted)
                    mapView.world?.worldFolder?.resolve("data/map_$mapId.dat")?.delete()
                    logSuccess("Deleted map with mapId $mapId")

                } ?: return@action sender.error("No map with mapId $mapId was found...")
            }
        }
        "info" {
            val mapId by intArg()
            action {
                extraCommands.plugin.server.getMap(mapId)?.let { mapView ->
                    sender.sendMessage("""
                        <gold>Map ID: <yellow>$mapId
                        <gold>Locked: <yellow>${mapView.isLocked}
                        <gold>Virtual: <yellow>${mapView.isVirtual}
                        <gold>Center-X: <yellow>${mapView.centerX}
                        <gold>Center-Z: <yellow>${mapView.centerZ}
                        """.trimIndent().miniMsg())
                } ?: return@action sender.error("No map with mapId $mapId was found...")
            }
        }
    }
}
