package service

import models.Sketch
import models.SketchModel
import models.User
import models.UserModel
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