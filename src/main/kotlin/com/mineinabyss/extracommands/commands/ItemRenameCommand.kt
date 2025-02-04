package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.brigadier.playerExecutes
import com.mineinabyss.idofront.items.editItemMeta
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.textcomponents.miniMsg
import com.mineinabyss.idofront.textcomponents.serialize
import com.mojang.brigadier.arguments.StringArgumentType
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

val EMOJY_ORIGINAL_ITEM_RENAME_TEXT = NamespacedKey.fromString("emojy:original_item_rename")!!
fun RootIdoCommands.itemRenameCommand() {
    "itemrename" {
        playerExecutes(StringArgumentType.greedyString().suggests {
            (context.source.executor as? Player)?.inventory?.itemInMainHand?.itemMeta?.displayName()?.serialize()?.let { suggestFiltering(it) }
        }.default { "" }) { renamed ->
            player.inventory.itemInMainHand.takeUnless(ItemStack::isEmpty)?.editItemMeta {
                if (renamed.isNullOrEmpty()) persistentDataContainer.remove(EMOJY_ORIGINAL_ITEM_RENAME_TEXT)
                else persistentDataContainer.set(EMOJY_ORIGINAL_ITEM_RENAME_TEXT, PersistentDataType.STRING, renamed)

                displayName(renamed.takeUnless { it.isNullOrEmpty() }?.miniMsg())
            } ?: sender.error("You must be holding an item to rename it!")
        }
    }
}
