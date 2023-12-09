package com.mineinabyss.extracommands.commands

import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.geary.papermc.datastore.decode
import com.mineinabyss.geary.papermc.datastore.encode
import com.mineinabyss.geary.papermc.datastore.remove
import com.mineinabyss.geary.papermc.tracking.items.components.SetItemIgnoredProperties
import com.mineinabyss.idofront.commands.arguments.intArg
import com.mineinabyss.idofront.commands.arguments.stringArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.items.editItemMeta
import com.mineinabyss.idofront.serialization.SerializableItemStack
import com.mineinabyss.idofront.textcomponents.miniMsg
import kotlin.math.max
import kotlin.math.min

fun CommandDSLEntrypoint.itemRenameCommand() {
    command("itemrename") {
        val renamed: String by stringArg { default = "" }
        playerAction {
            player.inventory.itemInMainHand.editItemMeta {
                if (renamed.isEmpty() || renamed == "null" ) displayName(null)
                else displayName(renamed.miniMsg())

                when {
                    hasDisplayName() -> {
                        // If has a display name set, stop display name migrations
                        val existingIgnore = persistentDataContainer.decode<SetItemIgnoredProperties>()?.ignore ?: setOf()
                        persistentDataContainer.encode(
                            SetItemIgnoredProperties(existingIgnore.plus(SerializableItemStack.Properties.DISPLAY_NAME))
                        )
                    }
                    else -> {
                        // If no display name, make sure we allow migrations again
                        val existingIgnore = persistentDataContainer.decode<SetItemIgnoredProperties>()?.ignore ?: return@editItemMeta
                        val ignore = existingIgnore.minus(SerializableItemStack.Properties.DISPLAY_NAME)
                        if (ignore.isEmpty()) persistentDataContainer.remove<SetItemIgnoredProperties>()
                        else persistentDataContainer.encode(SetItemIgnoredProperties(ignore))
                    }
                }
            }
        }
    }
}
