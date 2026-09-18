package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.*
import com.mineinabyss.idofront.messaging.info
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute

fun RootIdoCommands.onlineInfoCommand() {
    "online" {
        executes {
            sender.info("<gold>There are ${Bukkit.getOnlinePlayers().size} players online right now!")
        }
    }
}