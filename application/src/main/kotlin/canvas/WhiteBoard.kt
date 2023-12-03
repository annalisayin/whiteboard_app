package canvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import data.Rectangle
import data.TextBox
import data.TextBoxData
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
fun WhiteBoard(sketches: SnapshotStateList<Sketch>, textList: SnapshotStateList<TextBox>, rectList: SnapshotStateList<Rectangle>, inDelete: MutableState<Boolean>) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(WebSockets)
    }


    val insendingSketches: MutableList<Sketch> = mutableListOf()
    val inUsedColor = remember { mutableStateOf(0)}
    val brushSize = remember { mutableStateOf(1)}
    val currentText = remember {mutableStateOf("hello")}

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
                    currentTool.value = -1
                    CoroutineScope(Dispatchers.IO).launch {
                        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/rect") {
                            val recJson = Json.encodeToString(newRec)
                            send(Frame.Text(recJson))
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/rects") {
                            for (frame in incoming) {
                                frame as? Frame.Text ?: continue
                                val shapeJson = frame.readText()

                                // Deserialize the JSON string into a list of Sketch objects
                                val responseShape: Rectangle = Json.decodeFromString(shapeJson)
                                rectList.add(responseShape)
                                println("Current recList after receiving is $rectList")
                            }
                        }
                    }
                }
                if (currentTool.value == 2) {
//                    val newCir = Circle(offset = tapOffset, color = Color(inUsedColor.value), size = brushSize.value.dp)
//                    shapeList.add(newCir)
//                    currentTool.value = -1
                }
                if (currentTool.value == 3) {
//                    val newTri =
//                        Triangle(offset = tapOffset, color = Color(inUsedColor.value), size = brushSize.value.dp)
//                    shapeList.add(newTri)
//                    currentTool.value = -1
                }
                if (currentTool.value == 4) {
                    val newText = TextBoxData(
                        offsetX = tapOffset.x.toInt(),
                        offsetY = tapOffset.y.toInt(),
                        currentText.value,
                        color = inUsedColor.value,
                        size = brushSize.value,
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
                                val receivedTB: TextBoxData = Json.decodeFromString(tbJson)
                                println("Received textbox is: ${receivedTB}")
                                val newTextBox = TextBox(receivedTB, receivedTB.Id, inDelete)
                                textList.add(newTextBox)
                            }
                        }
                    }

                }
                if (currentTool.value == 5) {
                    currentTool.value = -1
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
//        shapeList.forEach { shape -> shape.draw() }
        rectList.forEach { r -> r.draw() }
        CoroutineScope(Dispatchers.IO).launch {
            println("Websocket /send-deleted-textbox-id")
            client.webSocket(
                method = HttpMethod.Get,
                host = "127.0.0.1",
                port = 8080,
                path = "/send-deleted-textbox-id"
            ) {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val dlTbJson = frame.readText()
                    println("Receiving dlTbJson is: $dlTbJson")
                    // Deserialize the JSON string into a list of Sketch objects
                    val receivedDLTBID: Int = Json.decodeFromString(dlTbJson)
                    println("Deleted textbox is: ${receivedDLTBID}")
                    textList.removeIf { it.Id == receivedDLTBID}
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            println("Websocket /send-updated-textbox")
            client.webSocket(
                method = HttpMethod.Get,
                host = "127.0.0.1",
                port = 8080,
                path = "/send-updated-textbox"
            ) {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val tbJson = frame.readText()
                    println("Receiving tbJson is: $tbJson")
                    // Deserialize the JSON string into a list of Sketch objects
                    val receivedTB: TextBoxData = Json.decodeFromString(tbJson)
                    println("Received textbox is: ${receivedTB}")
                    val newTextBox = TextBox(receivedTB, receivedTB.Id, inDelete)
                    textList.removeIf { it.Id == newTextBox.Id}
                    textList.add(newTextBox)
                }
            }
        }
        textList.forEach { text -> text.draw() }
    }
}