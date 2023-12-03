package service

import models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun findAllSketches(): List<ResultRow> {
    var sketches = emptyList<ResultRow>()
    transaction {
        sketches = SketchModel
            .selectAll()
            .toList()
    }
    return sketches
}

fun insertSketch(sketch: Sketch) {
    transaction {
        val id = SketchModel.insertAndGetId {
            it[startX] = sketch.startX
            it[startY] = sketch.startY
            it[endX] = sketch.endX
            it[endY] = sketch.endY
            it[color] = sketch.color
            it[width] = sketch.width
        }
    }
}

fun insertTextbox(tb: TextBox): Int {
    var tbId = -1
    transaction {
        val id = TBModel.insertAndGetId {
            it[offsetX] = tb.offsetX
            it[offsetY] = tb.offsetY
            it[curtext] = tb.curtext
            it[color] = tb.color
            it[size] = tb.size
            it[curId] = -1
        }
        TBModel.update({ TBModel.id eq id.value }) {
            it[curId] = id.value
        }
        tbId = id.value
    }
    return tbId
}

fun updateTextboxPositionById(id: Int, newOffsetX: Int, newOffsetY: Int) {
    transaction {
        TBModel.update({ TBModel.id eq id }) {
            it[offsetX] = newOffsetX
            it[offsetY] = newOffsetY
        }
    }
}

fun findAllTextboxes(): List<ResultRow> {
    var textboxes = emptyList<ResultRow>()
    transaction {
        textboxes = TBModel
            .selectAll()
            .toList()
    }
    return textboxes
}

fun deleteAll(): Unit {
    transaction {
        TBModel.deleteAll()
    }
}

fun deleteTextBoxById(id: Int): Unit {
    transaction {
        TBModel.deleteWhere { curId eq id }
    }
}

fun insertUser(user: User) {
    transaction {
        val id = UserModel.insertAndGetId {
            it[username] = user.username
        }
    }
}

fun findAllUsers(): List<ResultRow> {
    var users = emptyList<ResultRow>()
    transaction {
        users = UserModel
            .selectAll()
            .toList()
    }
    return users
}

fun findAllRects(): List<ResultRow> {
    var shapes = emptyList<ResultRow>()
    transaction {
        shapes = RectangleModel
            .selectAll()
            .toList()
    }
    return shapes
}

fun insertRectangle(s: Rectangle) {
    transaction {
        val id = RectangleModel.insertAndGetId {
            it[x] = s.x
            it[y] = s.y
            it[color] = s.color
            it[size] = s.size
        }
    }
}