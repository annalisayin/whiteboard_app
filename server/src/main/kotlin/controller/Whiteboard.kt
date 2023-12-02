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
import models.*
import service.*


fun Application.configureWhiteboard() {
    routing {
        val incomingSketches: MutableList<Sketch> = mutableListOf()
        val incomingTextboxes: MutableList<TextBox> = mutableListOf()
        val connectedUsers: MutableList<User> = mutableListOf()
        val incomingRects: MutableList<Rectangle> = mutableListOf()

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

        webSocket("/receive-textbox") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                println("receive-textbox has incoming data!")
                val receivedText = frame.readText()
                val textbox = Json.decodeFromString<TextBox>(receivedText)
                println("received this Textbox:${textbox}")
                insertTextbox(textbox)
                incomingTextboxes.add(textbox)
            }
        }

        webSocket("/send-textbox"){
            while (true) {
                if(incomingTextboxes.isNotEmpty()) {
                    println("send-textbox is being processed")
                    val toBeSentTB = incomingTextboxes.removeAt(0)
                    val tbJson = Json.encodeToString(toBeSentTB)
                    send(Frame.Text(tbJson))
                    println("Remaining of incomingTextboxes: ${incomingTextboxes}")
                }
                delay(10) // Introduce a delay between iterations to allow other coroutines to run
            }
        }

        get("/textbox-list") {
            val tbs = findAllTextboxes().map { row -> row.toTextBox() }
            val json = Json.encodeToString(tbs)
            call.respondText(json, contentType = ContentType.Application.Json)
        }

        get("/delete-textboxes") {
            deleteAll()
        }

        delete("/textbox/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                deleteTextBoxById(id)
                call.respond(HttpStatusCode.OK, "TextBox with Id $id deleted successfully")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
            }
        }

        webSocket("/user") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val users = Json.decodeFromString<List<User>>(receivedText)
                println(receivedText)
                for (user in users) {
                    insertUser(user)
                }
                connectedUsers.addAll(users)
            }
        }

        webSocket("/users") {
            while (true) {
                if (connectedUsers.isNotEmpty()) {
                    val userJson = Json.encodeToString(connectedUsers)
                    send(Frame.Text(userJson))
                    connectedUsers.clear()
                }
                delay(10) // Introduce a delay between iterations to allow other coroutines to run
            }
        }

        get("/connected-users") {
            val users = findAllUsers().map { row -> row.toUser() }
            val json = Json.encodeToString(users)
            call.respondText(json, contentType = ContentType.Application.Json)
        }

        webSocket("/rect") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val shapes = Json.decodeFromString<List<Rectangle>>(receivedText)
                println(shapes)
                for (s in shapes) {
                    insertRectangle(s)
                }
                incomingRects.addAll(shapes)
            }
        }

        webSocket("/rects") {
            while (true) {
                if (incomingRects.isNotEmpty()) {
                    val shapeJson = Json.encodeToString(incomingRects)
                    send(Frame.Text(shapeJson))
                    incomingRects.clear()
                }
                delay(10) // Introduce a delay between iterations to allow other coroutines to run
            }
        }


        get("/rects-list") {
            val shapes = findAllRects().map { row -> row.toRectangle() }
            val json = Json.encodeToString(shapes)
            call.respondText(json, contentType = ContentType.Application.Json)
        }

    }
}