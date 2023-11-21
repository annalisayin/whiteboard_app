package models
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object UserModel : IntIdTable() {
    val username = varchar("username", 50).uniqueIndex()
}

fun ResultRow.toUser(): User {
    return User(
        this[UserModel.username]
    )
}