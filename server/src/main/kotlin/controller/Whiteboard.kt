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
        val incomingDeletedTBId: MutableList<Int> = mutableListOf()
        val incomingUpdatedTBId: MutableList<TextBox> = mutableListOf()
        val incomingClearAllSignal: MutableList<Int> = mutableListOf()

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
                val tbId = insertTextbox(textbox)
                textbox.Id = tbId
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

        webSocket("/clear-whiteboard") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                println("clear-whiteboard has incoming data!")
                val receivedText = frame.readText()
                val signal = Json.decodeFromString<Int>(receivedText)
                println("clear whiteboard signal is:${signal}")
                deleteAll()
                incomingClearAllSignal.add(signal)
            }
        }

        webSocket("/send-clear-whiteboard-signal"){
            while (true) {
                if(incomingClearAllSignal.isNotEmpty()) {
                    println("send-clear-whiteboard-signal is being processed")
                    val toBeSentSignal = incomingClearAllSignal.removeAt(0)
                    val signalJson = Json.encodeToString(toBeSentSignal)
                    send(Frame.Text(signalJson))
                    println("Remaining of incomingClearAllSignal: ${incomingClearAllSignal}")
                }
                delay(10) // Introduce a delay between iterations to allow other coroutines to run
            }
        }

        delete("/delete-all") {
            deleteAll()
            call.respond(HttpStatusCode.OK, "Whiteboard is cleared successfully")
        }

        webSocket("/receive-deleted-texbox-id") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val deletedTextBoxId = Json.decodeFromString<Int>(receivedText)
                incomingDeletedTBId.add(deletedTextBoxId)
            }
        }

        webSocket("/send-deleted-textbox-id") {
            while (true) {
                if (incomingDeletedTBId.isNotEmpty()) {
                    val toBeSentDeletedTBId = incomingDeletedTBId.removeAt(0)
                    val dlIdJson = Json.encodeToString(toBeSentDeletedTBId)
                    send(Frame.Text(dlIdJson))
                    println("Remaining of incomingDeletedTBId: ${dlIdJson}")
                }
                delay(10) // Introduce a delay between iterations to allow other coroutines to run
            }
        }

        webSocket("/receive-updated-textbox") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                println("receive-textbox has incoming data!")
                val receivedText = frame.readText()
                val textbox = Json.decodeFromString<TextBox>(receivedText)
                println("received this Textbox:${textbox}")
                updateTextboxPositionById(textbox.Id, textbox.offsetX, textbox.offsetY)
                incomingUpdatedTBId.add(textbox)
            }
        }

        webSocket("/send-updated-textbox"){
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
                val r = Json.decodeFromString<Rectangle>(receivedText)
                insertRectangle(r)
                incomingRects.add(r)
            }
        }

        webSocket("/rects") {
            while (true) {
                if (incomingRects.isNotEmpty()) {
                    val toBeSentRec = incomingRects.removeAt(0)
                    val rJson = Json.encodeToString(toBeSentRec)
                    send(Frame.Text(rJson))
                    println("Remaining of incomingRects: ${incomingRects}")
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