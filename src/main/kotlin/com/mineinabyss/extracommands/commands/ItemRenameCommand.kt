package com.mineinabyss.extracommands.commands

import com.mineinabyss.geary.papermc.datastore.decode
import com.mineinabyss.geary.papermc.datastore.encode
import com.mineinabyss.geary.papermc.datastore.remove
import com.mineinabyss.geary.papermc.tracking.items.components.SetItemIgnoredProperties
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.ensureSenderIsPlayer
import com.mineinabyss.idofront.items.editItemMeta
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.serialization.SerializableItemStackProperties
import com.mineinabyss.idofront.textcomponents.miniMsg
import com.mineinabyss.idofront.textcomponents.serialize
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun CommandDSLEntrypoint.itemRenameCommand() {
    command("itemrename") {
        ensureSenderIsPlayer()
        action {
            val player = sender as? Player ?: return@action
            val renamed = arguments.joinToString(" ")
            player.inventory.itemInMainHand.takeIf { !it.isEmpty }?.editItemMeta {
                if (renamed.isEmpty()) displayName(null)
                else displayName(renamed.miniMsg())

                when {
                    hasDisplayName() -> {
                        // If has a display name set, stop display name migrations
                        val existingIgnore =
                            persistentDataContainer.decode<SetItemIgnoredProperties>()?.ignore ?: setOf()
                        persistentDataContainer.encode(
                            SetItemIgnoredProperties(existingIgnore.plus(SerializableItemStackProperties.DISPLAY_NAME))
                        )
                    }

                    else -> {
                        // If no display name, make sure we allow migrations again
                        val existingIgnore =
                            persistentDataContainer.decode<SetItemIgnoredProperties>()?.ignore ?: return@editItemMeta
                        val ignore = existingIgnore.minus(SerializableItemStackProperties.DISPLAY_NAME)
                        if (ignore.isEmpty()) persistentDataContainer.remove<SetItemIgnoredProperties>()
                        else persistentDataContainer.encode(SetItemIgnoredProperties(ignore))
                    }
                }
            } ?: sender.error("You must be holding an item to rename it!")
        }
    }
}

fun itemRenameTabComplete(sender: CommandSender, args: Array<out String>) =
    (sender as? Player)?.inventory?.itemInMainHand?.itemMeta?.displayName()?.serialize().takeIf { args.size == 1 }
        ?.let { listOf(it) } ?: emptyList()

