package com.mineinabyss.extracommands.extensions

import com.google.common.util.concurrent.AbstractScheduledService.Scheduler
import com.mineinabyss.extracommands.extraCommands
import com.mineinabyss.idofront.messaging.logError
import org.bukkit.entity.Player
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapPalette
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView
import java.awt.Color
import java.awt.Image
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class DeletedMapRenderer : MapRenderer() {

    var blockedImage: Image? = null
    var done = false

    fun loadImage(): Boolean {
        var image = kotlin.runCatching { ImageIO.read(URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzGPrduNCslB0T7mY-Dd3TgswqFnVzrkYN9LjplnnQ_Q&s")) }.getOrNull() ?: return false
        image = MapPalette.resizeImage(image)
        blockedImage = image
        return true
    }
    override fun render(map: MapView, canvas: MapCanvas, player: Player) {
        if (done) return
        blockedImage?.let {
            canvas.drawImage(0,0, it)
            done = true
        }
    }
}