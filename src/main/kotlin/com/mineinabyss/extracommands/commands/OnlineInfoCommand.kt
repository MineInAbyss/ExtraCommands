package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.info
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute

fun RootIdoCommands.onlineInfoCommand() {
    "online" {
        requiresPermission("extracommands.online")
        executes {
            sender.info("<gold>There are ${Bukkit.getOnlinePlayers().size} players online right now!")
        }
    }
}