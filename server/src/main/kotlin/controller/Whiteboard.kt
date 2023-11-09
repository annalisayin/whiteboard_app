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

fun Application.configureWhiteboard() {
    routing {
        var updateSketches = true
        webSocket("/sketch") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val sketch = Json.decodeFromString<Sketch>(receivedText)
                insertSketch(sketch) // Insert the received Sketch object into the database
                print("received sketch")
                updateSketches = true
            }
        }

        webSocket("/sketches") {
            while(updateSketches) {
                val updatedSketches: List<Sketch> = findAllSketches().map { row -> row.toSketch() }
                println("get updated sketches")
                // Convert the list of sketches to JSON
                val sketchesJson = Json.encodeToString(updatedSketches)
                print("prepare to send back sketches")
                // Send the JSON data to the client
                send(Frame.Text(sketchesJson))
                print("sending back sketch")
                updateSketches = false
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