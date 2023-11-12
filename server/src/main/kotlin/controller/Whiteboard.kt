package controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Sketch
import models.toSketch
import service.findAllSketches
import service.insertSketch

fun Application.configureWhiteboard() {
    routing {
        val incomingSketches: MutableList<Sketch> = mutableListOf()
        webSocket("/sketch") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val sketches = Json.decodeFromString<List<Sketch>>(receivedText)
                println(sketches)
                for (sketch in sketches) {
                    insertSketch(sketch)
                }
                incomingSketches.addAll(sketches)
            }
        }

        webSocket("/sketches") {
            while (true) {
                if (incomingSketches.isNotEmpty()) {
                    val sketchJson = Json.encodeToString(incomingSketches)
                    send(Frame.Text(sketchJson))
                    incomingSketches.clear()
                }
                delay(10) // Introduce a delay between iterations to allow other coroutines to run
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