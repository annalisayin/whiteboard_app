package controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Sketch
import models.toSketch
import service.findAllSketches
import service.insertSketch

fun Application.configureWhiteboard() {

    routing {
        get("/sketches") {
            val sketches = findAllSketches().map { row -> row.toSketch() }
            val json = Json.encodeToString(sketches)
            call.respondText(json, contentType = ContentType.Application.Json)
        }

        // POST endpoint to insert a new sketch
        post("/sketch") {
            val sketchRequest = call.receive<Sketch>() // Receive the JSON request and deserialize it to a Sketch object
            insertSketch(sketchRequest) // Insert the received sketch into the database
            call.respondText("Sketch inserted successfully", status = HttpStatusCode.Created)
        }
    }
}