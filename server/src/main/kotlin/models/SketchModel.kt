package models
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object SketchModel : IntIdTable() {
    val startX = float("startX")
    val startY = float("startY")
    val endX = float("endX")
    val endY = float("endY")
    val color = integer("color")
    val width = integer("width")
}

fun ResultRow.toSketch(): Sketch {
    return Sketch(
        this[SketchModel.startX],
        this[SketchModel.startY],
        this[SketchModel.endX],
        this[SketchModel.endY],
        this[SketchModel.color],
        this[SketchModel.width]
    )
}