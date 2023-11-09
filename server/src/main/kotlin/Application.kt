
import controller.configureWhiteboard
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import models.SketchModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun connectToDatabase() {
    Database.connect("jdbc:sqlite:whiteboard.db")
}

fun main() {
    connectToDatabase()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    transaction {
        SchemaUtils.create(SketchModel)
        // Test one random record
        SketchModel.insert {
            it[startX] = 0.1f
            it[startY] = 0.2f
            it[endX] = 0.3f
            it[endY] = 0.4f
            it[color] = 0
            it[width] = 1
        }
    }
    configureWhiteboard()
}
