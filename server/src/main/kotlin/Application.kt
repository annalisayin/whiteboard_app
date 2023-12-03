
import controller.configureWhiteboard
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.websocket.*
import models.RectangleModel
import models.SketchModel
import models.TBModel
import models.UserModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun connectToDatabase() {
    Database.connect("jdbc:sqlite:whiteboard.db")
//    val url = "jdbc:postgresql://pg-167c54e4-cs346.a.aivencloud.com:10691/defaultdb?sslmode=require"
//    val user = "avnadmin"
//    val password = "AVNS_d5a--ZxRNqu7WNAzEgg"
//
//    Database.connect(url, driver = "org.postgresql.Driver", user = user, password = password)
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
        SchemaUtils.create(RectangleModel)
        SchemaUtils.create(TBModel)
    }
    configureWhiteboard()
}
