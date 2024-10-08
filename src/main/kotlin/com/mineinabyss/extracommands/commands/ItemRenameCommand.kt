package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.items.editItemMeta
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.textcomponents.miniMsg
import com.mineinabyss.idofront.textcomponents.serialize
import com.mojang.brigadier.arguments.StringArgumentType
import org.bukkit.entity.Player

fun RootIdoCommands.itemRenameCommand() {
    "itemrename" {
        requiresPermission("extracommands.itemrename")
        val renamed by StringArgumentType.greedyString().suggests {
            (context.source.executor as? Player)?.inventory?.itemInMainHand?.itemMeta?.displayName()?.serialize()?.let { suggestFiltering(it) }
        }
        playerExecutes {
            player.inventory.itemInMainHand.takeIf { !it.isEmpty }?.editItemMeta {
                displayName(renamed()?.miniMsg())
            } ?: sender.error("You must be holding an item to rename it!")
        }
    }
}
