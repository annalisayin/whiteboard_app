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
import models.Sketch
import models.toSketch
import service.findAllSketches
import service.insertSketch
import service.insertUser
import models.User
import models.toUser
import service.findAllUsers


fun Application.configureWhiteboard() {
    routing {
        val incomingSketches: MutableList<Sketch> = mutableListOf()
        val connectedUsers: MutableList<User> = mutableListOf()

//        val clientsChannel = Channel<Set<User>>(Channel.BUFFERED)
//
//        fun DefaultWebSocketSession.sendUpdateToClient(users: Set<User>) {
//            val userJson = Json.encodeToString(users.toList())
//
//            // Launch a coroutine to send the update
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    send(Frame.Text(userJson))
//                } catch (e: Exception) {
//                    println("Error sending update to client: $e")
//                }
//            }
//        }
//
//        suspend fun DefaultWebSocketSession.sendUpdateToClients() {
//            clientsChannel.send(connectedUsers)
//        }
//
//        webSocket("/user") {
//            try {
//                for (frame in incoming) {
//                    if (frame is Frame.Text) {
//                        val receivedText = frame.readText()
//
//                        val username = Json.decodeFromString<User>(receivedText)
//
//                        insertUser(username)
//                        connectedUsers.add(username)
//
//                        // Send updates to all connected clients
//                        sendUpdateToClients()
//
//                        println("Connected Users: $connectedUsers")
//
//                        send(Frame.Text("Welcome, $username!"))
//                    }
//                }
//            } catch (e: Exception) {
//                println("Error handling user WebSocket: $e")
//            }
//        }
//
//        webSocket("/users") {
//            try {
//                for (frame in incoming) {
//                    frame as? Frame.Text ?: continue
//                    sendUpdateToClient(connectedUsers)
//                }
//            } catch (e: Exception) {
//                println("Error handling users WebSocket: $e")
//            }
//        }
//
//        get("/connected-users") {
//            val users = findAllUsers().map { row -> row.toUser() }
//            val json = Json.encodeToString(users)
//            call.respondText(json, contentType = ContentType.Application.Json)
//        }

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

        // POST endpoint to insert a new sketch
//        post("/sketch") {
//            val sketchRequest = call.receive<Sketch>() // Receive the JSON request and deserialize it to a Sketch object
//            insertSketch(sketchRequest) // Insert the received sketch into the database
//            call.respondText("Sketch inserted successfully", status = HttpStatusCode.Created)
//        }

//        post("/users") {
//            try {
//                val user = call.receive<User>()
//                insertUser(user) // Call a function to insert the user into the database (you need to implement this)
//                call.respond(HttpStatusCode.Created, "User created successfully")
//            } catch (e: Exception) {
//                call.respond(HttpStatusCode.BadRequest, "Failed to create user: ${e.message}")
//            }
//        }
    }
}