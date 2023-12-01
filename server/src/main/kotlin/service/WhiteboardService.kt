package service

import models.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
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