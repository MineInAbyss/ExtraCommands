package com.mineinabyss.extracommands.commands

import com.mineinabyss.geary.papermc.datastore.decode
import com.mineinabyss.geary.papermc.datastore.encode
import com.mineinabyss.geary.papermc.datastore.remove
import com.mineinabyss.geary.papermc.tracking.items.components.SetItemIgnoredProperties
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.ensureSenderIsPlayer
import com.mineinabyss.idofront.items.editItemMeta
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.serialization.SerializableItemStackProperties
import com.mineinabyss.idofront.textcomponents.miniMsg
import com.mineinabyss.idofront.textcomponents.serialize
import com.mojang.brigadier.arguments.StringArgumentType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun RootIdoCommands.itemRenameCommand() {
    "itemrename" {
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
