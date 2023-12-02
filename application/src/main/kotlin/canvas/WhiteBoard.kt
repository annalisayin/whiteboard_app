package canvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import data.Rectangle
import data.Shape
import data.TextBox
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
import toolbar.ToolSelection

var socket: WebSocketSession? = null

@Composable
fun WhiteBoard(initialSketches: List<Sketch>, initialTextboxes: List<TextBox>) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(WebSockets)
    }

    val sketches = remember { mutableStateListOf<Sketch>() }
    val shapeList = remember { mutableStateListOf<Shape>()}
    val rectList = remember { mutableStateListOf<Rectangle>()}

//    runBlocking {
//        try {
//            val responseRects: List<Rectangle> = Json.decodeFromString(client.get("http://localhost:8080/rects-list").body())
//            responseRects.forEach { r: Rectangle -> rectList.add(r) }
//        }
//        catch (e: Exception) {
//            println(e)
//        }
//    }
    val textList = remember { mutableStateListOf<TextBox>()}
    sketches.addAll(initialSketches)
    textList.addAll(initialTextboxes)


    val insendingSketches: MutableList<Sketch> = mutableListOf()
    val insendingRect: MutableList<Rectangle> = mutableListOf()
    val inUsedColor = remember { mutableStateOf(0)}
    val brushSize = remember { mutableStateOf(1)}
    val currentText = remember {mutableStateOf("hello")}
    val inDelete = remember { mutableStateOf(false)}

    val focusManager = LocalFocusManager.current

    val currentTool = remember {mutableStateOf(-1)}

    ToolSelection(currentTool, inUsedColor, brushSize, currentText, inDelete)

    Box(modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { tapOffset ->
                if (currentTool.value == 1) {
                    val newRec =
                        Rectangle(x = tapOffset.x, y = tapOffset.y, color = inUsedColor.value, size = brushSize.value)
                    shapeList.add(newRec)
                    currentTool.value = -1
                    CoroutineScope(Dispatchers.IO).launch {
                        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/rect") {
                            val shapesList = insendingRect.toList()
                            val shapesJson = Json.encodeToString(shapesList)
                            send(Frame.Text(shapesJson))
                            insendingRect.clear()
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/rects") {
                            for (frame in incoming) {
                                frame as? Frame.Text ?: continue
                                val shapeJson = frame.readText()

                                // Deserialize the JSON string into a list of Sketch objects
                                val responseShape: MutableList<Rectangle> = Json.decodeFromString(shapeJson)
                                rectList.addAll(responseShape)
                            }
                        }
                    }
                }
//                if (currentTool.value == 2) {
//                    val newCir = Circle(offset = tapOffset, color = Color(inUsedColor.value), size = brushSize.value.dp)
//                    shapeList.add(newCir)
//                    currentTool.value = -1
//                }
//                if (currentTool.value == 3) {
//                    val newTri =
//                        Triangle(offset = tapOffset, color = Color(inUsedColor.value), size = brushSize.value.dp)
//                    shapeList.add(newTri)
//                    currentTool.value = -1
//                }
                if (currentTool.value == 4) {
                    val newText = TextBox(
                        offsetX = tapOffset.x.toInt(),
                        offsetY = tapOffset.y.toInt(),
                        currentText.value,
                        color = inUsedColor.value,
                        size = brushSize.value,
                        Id = null,
                    )
                    println("NewText is: ${newText}")
                    currentTool.value = -1
                    CoroutineScope(Dispatchers.IO).launch {
                        println("Websocket /receive-textbox")
                        val socket: DefaultClientWebSocketSession = client.webSocketSession(
                            method = HttpMethod.Get,
                            host = "127.0.0.1",
                            port = 8080,
                            path = "/receive-textbox"
                        )
                        println("TextBox to be sent:$newText")
                        val tbJson = Json.encodeToString(newText)
                        socket.send(Frame.Text(tbJson))
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        println("Websocket /send-textbox")
                        client.webSocket(
                            method = HttpMethod.Get,
                            host = "127.0.0.1",
                            port = 8080,
                            path = "/send-textbox"
                        ) {
                            for (frame in incoming) {
                                frame as? Frame.Text ?: continue
                                val tbJson = frame.readText()
                                println("Receiving tbJson is: $tbJson")
                                // Deserialize the JSON string into a list of Sketch objects
                                val receivedTB: TextBox = Json.decodeFromString(tbJson)
                                println("Received textbox is: ${receivedTB}")
                                textList.add(receivedTB)
                            }
                        }
                    }

                }

//                    focusManager.clearFocus()

            }
        }, //contentAlignment = Alignment.TopStart

    ) {
//        // "fake" canvas just used for pen tool
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(true) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                    change.consume()
                    if (currentTool.value == 0) {
                        val startOffset = change.position - dragAmount
                        val endOffset = change.position
                        val sketch = Sketch(
                            startX = startOffset.x,
                            startY = startOffset.y,
                            endX = endOffset.x,
                            endY = endOffset.y,
                            color = inUsedColor.value,
                            width = brushSize.value,
                        )
                        insendingSketches.add(sketch)
                    }
                },
                onDragEnd = {
                    if (currentTool.value == 0) {

                        CoroutineScope(Dispatchers.IO).launch {
                            val socket: DefaultClientWebSocketSession = client.webSocketSession(
                                method = HttpMethod.Get,
                                host = "127.0.0.1",
                                port = 8080,
                                path = "/sketch"
                            )
                            val sketchesList = insendingSketches.toList()
                            val sketchesJson = Json.encodeToString(sketchesList)
                            socket.send(Frame.Text(sketchesJson))
                            insendingSketches.clear()
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            client.webSocket(
                                method = HttpMethod.Get,
                                host = "127.0.0.1",
                                port = 8080,
                                path = "/sketches"
                            ) {
                                for (frame in incoming) {
                                    frame as? Frame.Text ?: continue
                                    val sketchJson = frame.readText()

                                    // Deserialize the JSON string into a list of Sketch objects
                                    val responseSketch: MutableList<Sketch> = Json.decodeFromString(sketchJson)
                                    sketches.addAll(responseSketch)
                                }
                            }
                        }
//                    }
                    }
                }
                )

            }
        )
        {
            sketches.forEach { sketch ->
                val colorInt = (sketch.color * 0xFFFFFF / 100) or 0xFF000000.toInt()
                drawLine(
                    color = Color(colorInt),
                    start = Offset(sketch.startX, sketch.startY),
                    end = Offset(sketch.endX, sketch.endY),
                    strokeWidth = sketch.width.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        shapeList.forEach { shape -> shape.draw() }
        rectList.forEach { r -> r.draw() }
        textList.forEach { text -> text.draw() }
    }
}