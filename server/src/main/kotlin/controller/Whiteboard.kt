package controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Sketch
import models.toSketch
import service.findAllSketches
import service.insertSketch
import java.util.*

fun Application.configureWhiteboard() {
    routing {
        val incomingSketch: Queue<Sketch> = LinkedList<Sketch>()
        webSocket("/sketch") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val sketch = Json.decodeFromString<Sketch>(receivedText)
                insertSketch(sketch) // Insert the received Sketch object into the database
                incomingSketch.add(sketch)
            }
        }

        webSocket("/sketches") {
            while(incomingSketch.size > 0) {
                val sendingSketch: Sketch? = incomingSketch.poll()
                if (sendingSketch != null) {
                    val sketchJson = Json.encodeToString(sendingSketch)
                    send(Frame.Text(sketchJson))
                }
            }
        }

        get("/sketches-list") {
            val sketches = findAllSketches().map { row -> row.toSketch() }
            val json = Json.encodeToString(sketches)
            call.respondText(json, contentType = ContentType.Application.Json)
        }

        // POST endpoint to insert a new sketch
//        post("/sketch") {
//            val sketchRequest = call.receive<Sketch>() // Receive the JSON request and deserialize it to a Sketch object
//            insertSketch(sketchRequest) // Insert the received sketch into the database
//            call.respondText("Sketch inserted successfully", status = HttpStatusCode.Created)
//        }
    }
}