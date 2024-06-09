package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.brigadier.RootIdoCommands
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.info
import org.bukkit.GameMode

fun RootIdoCommands.mineInAbyssCommands() {
    "orespawns" {
        playerExecutes {
            player.info("""
                <#00bfff><b>You can find the different ore spawnrates here:</b>
                <click:open_url:'https://mineinabyss.com/orespawns'><#ffd500><underlined>https://mineinabyss.com/orespawns
                """.trimIndent())
        }
    }
    "mobdrops" {
        playerExecutes {
            player.info("""
                <#37a600><b>All the mobdrops can be found here:</b>
                <click:open_url:'https://mineinabyss.com/mobdrops'><#6ff030><underlined>https://mineinabyss.com/mobdrops
            """.trimIndent())
        }
    }
    "villagertrades" {
        playerExecutes {
            player.info("""
                <#4c00fc><b>The Orth Trading prices can be found here:</b>
                <click:open_url:'https://mineinabyss.com/villagertrades'><#008bfc><underlined>https://mineinabyss.com/villagertrades
            """.trimIndent())
        }
    }
    "website" {
        playerExecutes {
            player.info("""
                <#4c00fc><b>The server's website can be found here:</b>
                <click:open_url:'https://mineinabyss.com'><#008bfc><underlined>https://www.mineinabyss.com
            """.trimIndent())
        }
    }
}
