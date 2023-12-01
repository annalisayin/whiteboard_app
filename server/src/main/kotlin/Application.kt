
import controller.configureWhiteboard
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.websocket.*
import models.SketchModel
import models.UserModel
import models.TBModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
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
    install(WebSockets)
    transaction {
        SchemaUtils.create(SketchModel)
        SchemaUtils.create(UserModel)
        SchemaUtils.create(TBModel)
    }
    configureWhiteboard()
}
