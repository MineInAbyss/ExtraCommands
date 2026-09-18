package com.mineinabyss.extracommands.commands

import com.mineinabyss.chatty.helpers.buildTagResolver
import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.textcomponents.miniMsg
import com.mineinabyss.idofront.textcomponents.serialize
import com.mojang.brigadier.arguments.StringArgumentType
import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

val EMOJY_ORIGINAL_ITEM_RENAME_TEXT = NamespacedKey.fromString("emojy:original_item_rename")!!
fun RootIdoCommands.itemRenameCommand() {
    "itemrename" {
        executes.asPlayer().args("name" to StringArgumentType.greedyString().suggests {
            (context.source.executor as? Player)?.inventory?.itemInMainHand?.getData(DataComponentTypes.CUSTOM_NAME)?.serialize()?.let { suggestFiltering(it) }
        }.default { "" }) { renamed ->
            val itemStack = player.inventory.itemInMainHand.takeUnless(ItemStack::isEmpty)
                ?: return@args sender.error("You must be holding an item to rename it!")

            itemStack.editPersistentDataContainer { pdc ->
                if (renamed.isNullOrEmpty()) pdc.remove(EMOJY_ORIGINAL_ITEM_RENAME_TEXT)
                else pdc.set(EMOJY_ORIGINAL_ITEM_RENAME_TEXT, PersistentDataType.STRING, renamed)
                val tagResolver = player.buildTagResolver()

                if (renamed.isNullOrEmpty()) itemStack.resetData(DataComponentTypes.CUSTOM_NAME)
                else itemStack.setData(DataComponentTypes.CUSTOM_NAME, renamed.miniMsg(tagResolver))
            }
        }
    }
}
