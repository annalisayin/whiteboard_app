package models
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object RectangleModel : IntIdTable() {
    val x = float("startX")
    val y = float("startY")
    val color = integer("color")
    val size = integer("size")
}

fun ResultRow.toRectangle(): Rectangle {
    return Rectangle(
        this[RectangleModel.x],
        this[RectangleModel.y],
        this[RectangleModel.color],
        this[RectangleModel.size]
    )
}