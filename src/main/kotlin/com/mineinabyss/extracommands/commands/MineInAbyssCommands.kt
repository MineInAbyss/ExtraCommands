package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.messaging.info

fun RootIdoCommands.mineInAbyssCommands() {
    "wiki" {
        requiresPermission("")
        playerExecutes {
            player.info("""
                <#00bfff><b>The MineInAbyss Wiki can be found here: </b>
                <click:open_url:'https://wiki.mineinabyss.com/'><#ffd500><underlined>https://wiki.mineinabyss.com/
            """.trimIndent())
        }
    }
}
