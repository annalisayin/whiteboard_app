package models
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object TBModel : IntIdTable() {
    val offsetX = integer("offsetx")
    val offsetY = integer("offsety")
    val curtext = text("curtext")
    val color = integer("color")
    val size = integer("size")
    val curId = integer("curId")
}

fun ResultRow.toTextBox(): TextBox {
    return TextBox(
        this[TBModel.offsetX],
        this[TBModel.offsetY],
        this[TBModel.curtext],
        this[TBModel.color],
        this[TBModel.size],
        this[TBModel.curId]
    )
}