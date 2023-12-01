package controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.*
import service.*


fun Application.configureWhiteboard() {
    routing {
        val incomingSketches: MutableList<Sketch> = mutableListOf()
        val incomingTextboxes: MutableList<TextBox> = mutableListOf()
        val connectedUsers: MutableList<User> = mutableListOf()

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
            while (incomingSketches.isNotEmpty()) {
                println("/SKETCHES-LIST ENDPOINT CALLED")
//                if (incomingSketches.isNotEmpty()) {
                    val sketchJson = Json.encodeToString(incomingSketches)
                    send(Frame.Text(sketchJson))
                    incomingSketches.clear()
//                }
                delay(10) // Introduce a delay between iterations to allow other coroutines to run
            }
        }

        get("/sketches-list") {
            println("/SKETCHES-LIST ENDPOINT CALLED")
            delay(1000L)
            val sketches = findAllSketches().map { row -> row.toSketch() }
            val json = Json.encodeToString(sketches)
            call.respondText(json, contentType = ContentType.Application.Json)
        }

        webSocket("/receive-textbox") {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val textboxes = Json.decodeFromString<List<TextBox>>(receivedText)
                for(tb in textboxes) {
                    println(tb)
                    insertTextbox(tb)
                }
                incomingTextboxes.addAll(textboxes)
            }
        }

        webSocket("/send-textbox"){
            println("/send-textbox ENDPOINT CALLED")
            delay(1000L)
            while (incomingTextboxes.isNotEmpty()) {
                    //println(incomingTextboxes)
                    val tbJson = Json.encodeToString(incomingTextboxes)
                    println("sending textboxes: " + tbJson)
                    send(Frame.Text(tbJson))
                    incomingTextboxes.clear()

                delay(1000L) // Introduce a delay between iterations to allow other coroutines to run
            }
        }

        get("/textbox-list") {
            val tbs = findAllTextboxes().map { row -> row.toTextBox() }
            val json = Json.encodeToString(tbs)
            call.respondText(json, contentType = ContentType.Application.Json)
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

    }
}