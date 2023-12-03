
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import canvas.Sketch
import canvas.WhiteBoard
import data.Rectangle
import data.TextBox
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json


@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(WebSockets)
    }
    var initialSketches = remember { mutableStateListOf<Sketch>() }
    var initialTextboxes = remember { mutableStateListOf<TextBox>()}
    var initialRects = remember { mutableStateListOf<Rectangle>()}
    runBlocking {
        try {
            println("Getting all sketches")
            val responseSketches: List<Sketch> = Json.decodeFromString(client.get("http://localhost:8080/sketches-list").body())
            responseSketches.forEach { sketch: Sketch -> initialSketches.add(sketch) }
        }
        catch (e: Exception) {
            println(e)
        }
    }

    runBlocking {
        try {
            println("Getting all textboxes")
            val responseTextboxes: List<TextBox> = Json.decodeFromString(client.get("http://localhost:8080/textbox-list").body())
            responseTextboxes.forEach {textbox: TextBox -> initialTextboxes.add(textbox)}
        }
        catch (e: Exception) {
            println(e)
        }
    }

    runBlocking {
        try {
            println("Getting all rectangles")
            val responseRects: List<Rectangle> = Json.decodeFromString(client.get("http://localhost:8080/rects-list").body())
            responseRects.forEach { r: Rectangle -> initialRects.add(r) }
        }
        catch (e: Exception) {
            println(e)
        }
    }

    val windowState = rememberWindowState(size = DpSize.Unspecified)
    Window(onCloseRequest = ::exitApplication, state = windowState) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            WhiteBoard(initialSketches, initialTextboxes, initialRects)
        }
        //App()
        //Canvas()
    }
}
