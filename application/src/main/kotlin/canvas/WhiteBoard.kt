package canvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import data.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
 import toolbar.ToolSelection

@Composable
fun WhiteBoard() {
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

    runBlocking {
        try {
            val responseSketches: List<Sketch> = Json.decodeFromString(client.get("http://localhost:8080/sketches-list").body())
            val responseRects: List<Rectangle> = Json.decodeFromString(client.get("http://localhost:8080/rects-list").body())
            responseSketches.forEach { sketch: Sketch -> sketches.add(sketch) }
            responseRects.forEach { r: Rectangle -> rectList.add(r) }
        }
        catch (e: Exception) {
            println(e)
        }
    }

    val insendingSketches: MutableList<Sketch> = mutableListOf()
    val insendingRect: MutableList<Rectangle> = mutableListOf()
    val inUsedColor = remember { mutableStateOf(0)}
    val brushSize = remember { mutableStateOf(1)}
    val textList = remember { mutableStateListOf<TextBox>()}
    val currentText = remember {mutableStateOf("hello")}
 //   val deleteObjects = remember { mutableStateOf(false)}

    val focusManager = LocalFocusManager.current

    val currentTool = remember {mutableStateOf(-1)}
    //val recentObject = remember { mutableStateListOf<Number>()}
    /* -1 = default (none)
        0 = pen
        1 = rectangle
        2 = circle
        3 = triangle
        4 = text */

    ToolSelection(currentTool, inUsedColor, brushSize, currentText)

    Box(modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    if ( currentTool.value == 1) {
                         val newRec = Rectangle(x = tapOffset.x, y = tapOffset.y, color = inUsedColor.value, size = brushSize.value)
                         insendingRect.add(newRec)
                        currentTool.value = -1
                    }
                    if ( currentTool.value == 2) {
                        val newCir = Circle(x = tapOffset.x, y = tapOffset.y, color = inUsedColor.value, size = brushSize.value)
                     //   insendingShapes.add(newCir)
                        currentTool.value = -1
                    }
                    if ( currentTool.value == 3 ) {
                        val newTri = Triangle(x = tapOffset.x, y = tapOffset.y, color = inUsedColor.value, size = brushSize.value)
                    //    insendingShapes.add(newTri)
                        currentTool.value = -1
                    }
                    if (currentTool.value == 4){
                        println(currentText.value)
                        val newText = TextBox(offset = tapOffset, currentText.value, color = Color(inUsedColor.value), size = brushSize.value.dp)
                        textList.add(newText)
                        currentTool.value = -1
                    }

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

                    focusManager.clearFocus()
                }
            )
        }, //contentAlignment = Alignment.TopStart

    ) {
        // "fake" canvas just used for pen tool
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

                    CoroutineScope(Dispatchers.IO).launch {
                        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/sketch") {
                            val sketchesList = insendingSketches.toList()
                            val sketchesJson = Json.encodeToString(sketchesList)
                            send(Frame.Text(sketchesJson))
                            insendingSketches.clear()
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/sketches") {
                            for (frame in incoming) {
                                frame as? Frame.Text ?: continue
                                val sketchJson = frame.readText()

                                // Deserialize the JSON string into a list of Sketch objects
                                val responseSketch: MutableList<Sketch> = Json.decodeFromString(sketchJson)
                                sketches.addAll(responseSketch)
                            }
                        }
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