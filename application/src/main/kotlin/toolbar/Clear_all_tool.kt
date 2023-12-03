package toolbar


import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun Clear_all_tool(inClearAll: MutableState<Boolean>, currentTool: MutableState<Int>) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(WebSockets)
    }
    remember { mutableStateOf(false) }
    val color = if (inClearAll.value == true) Color.Green else Color.Gray
    remember {
        mutableStateOf("Show Pop Up")
    }
    Button(
        modifier = Modifier
            .padding(30.dp),
        onClick = {
            currentTool.value = if (currentTool.value == 6) -1 else 6
            inClearAll.value = if (inClearAll.value == true) false else true
            println("inClearAll is: $inClearAll")
            if (inClearAll.value == true) {
                CoroutineScope(Dispatchers.IO).launch {
                    val signal = 1
                    println("Websocket /clear-whiteboard")
                    val socket: DefaultClientWebSocketSession = client.webSocketSession(
                        method = HttpMethod.Get,
                        host = "127.0.0.1",
                        port = 8080,
                        path = "/clear-whiteboard"
                    )
                    println("Signal to be sent:$signal")
                    val signalJson = Json.encodeToString(signal)
                    socket.send(Frame.Text(signalJson))
                }
            }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
        Icon(Icons.Filled.Clear, contentDescription = "Localized description")
    }
}