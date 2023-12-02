package com.mineinabyss.extracommands.commands

import com.mineinabyss.idofront.commands.arguments.enumArg
import com.mineinabyss.idofront.commands.entrypoint.CommandDSLEntrypoint
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.messaging.info
import org.bukkit.GameMode

fun CommandDSLEntrypoint.mineInAbyssCommands() {
    command("orespawns") {
        playerAction {
            player.info("""
                <#00bfff><b>You can find the different ore spawnrates here:
                <#ffd500><underlined>https://mineinabyss.com/orespawns
                """.trimIndent())
        }
    }
    command("mobdrops") {
        playerAction {
            player.info("""
                <#37a600><b>All the mobdrops can be found here:
                <#6ff030><underlined>https://mineinabyss.com/mobdrops
            """.trimIndent())
        }
    }
    command("villagertrades") {
        playerAction {
            player.info("""
                <#4c00fc><b>The Orth Trading prices can be found here:
                <#008bfc><underlined>https://mineinabyss.com/villagertrades
            """.trimIndent())
        }
    }
    command("website") {
        playerAction {
            player.info("""
                <#4c00fc><b>The server's website can be found here:
                <#008bfc><underlined>https://www.mineinabyss.com
            """.trimIndent())
        }
    }
}
