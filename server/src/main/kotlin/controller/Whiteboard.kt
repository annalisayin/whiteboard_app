package controller

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import service.findAllSketches

fun Application.configureWhiteboard() {

    routing {
        get("/whiteboard") {
            val sketches = findAllSketches()
            call.respondText(sketches.toString())
        }
    }
}