package net.codebot.models.canvas
import org.jetbrains.exposed.dao.id.IntIdTable

object SketchModel : IntIdTable() {
    val startX = float("startX")
    val startY = float("startY")
    val endX = float("endX")
    val endY = float("endY")
    val color = integer("color")
    val width = integer("width")
}