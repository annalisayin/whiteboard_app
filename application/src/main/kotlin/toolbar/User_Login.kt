package toolbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import data.User
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun User_Login() {
    var text by remember { mutableStateOf(TextFieldValue()) }
    var connectedUsers = remember { mutableStateListOf<User>() }
    val client = HttpClient(CIO) {
        install(WebSockets)
    }
    LaunchedEffect(Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/update-user-session") {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val userJson = frame.readText()

                    // Deserialize the JSON string into a list of User objects
                    val responseUser: MutableList<User> = Json.decodeFromString(userJson)
                    connectedUsers.clear()
                    connectedUsers.addAll(responseUser)
                }
            }
        }
    }
//    LaunchedEffect(Unit) {
//        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/users") {
//            for (frame in incoming) {
//                frame as? Frame.Text ?: continue
//                val userJson = frame.readText()
//
//                // Deserialize the JSON string into a list of User objects
//                val responseUsers: List<User> = Json.decodeFromString(userJson)
//
//                // Use a set to store unique users
//                val uniqueUsers = responseUsers.toSet()
//
//                // Update the state with the new users
//                connectedUsers.clear()
//                connectedUsers.addAll(uniqueUsers)
//
//                // Print or use the updated list of connected users
//                println("Connected Users: $connectedUsers")
//            }
//        }
//    }


    Row() {
        Column(modifier = Modifier) {
            Text("Connected Users:")
            Spacer(modifier = Modifier.height(15.dp))
            for (u in connectedUsers) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(u.username)
                }
            }
        }
        Spacer(modifier = Modifier.width(15.dp))
        Column(
            modifier = Modifier
        ) {
            Text("Enter User Login")
            Spacer(modifier = Modifier.height(15.dp))
            TextField(
                modifier = Modifier,
                value = text,
                onValueChange = {
                    text = it
                },
                enabled = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Username"
                    )
                },
                singleLine = true
            )
            Button(
                onClick = {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/user") {
//                            println("WebSocket connection established")
//
//                            // Send a message
//                            val userJson = Json.encodeToString(User(text.text))
//                            send(Frame.Text(userJson))
//
//                            // Handle incoming messages
//                            for (frame in incoming) {
//                                if (frame is Frame.Text) {
//                                    val receivedText = frame.readText()
//                                    println("Received message: $receivedText")
//                                    // Process the received message as needed
//                                }
//                            }
//
//                            println("WebSocket connection closed")
//                        }
//                    }
                    val newUser = User(text.text)
                    CoroutineScope(Dispatchers.IO).launch {
                        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/new-user") {
                            val usersJson = Json.encodeToString(newUser)
                            println("Sending user data: $newUser")
                            send(Frame.Text(usersJson))
                        }
                    }
                }
            ) { Text("Enter") }
        }
    }
}
