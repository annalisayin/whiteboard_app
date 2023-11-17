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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
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
    runBlocking {
        try {
            val responseSketches: List<Sketch> = Json.decodeFromString(client.get("http://localhost:8080/sketches-list").body())
            responseSketches.forEach { sketch: Sketch -> sketches.add(sketch) }
        }
        catch (e: Exception) {
            println(e)
        }
    }
    val insendingSketches: MutableList<Sketch> = mutableListOf()
    val sketchStatus = remember { mutableStateOf(false) }
    val inUsedColor = remember { mutableStateOf(0)}
    val brushSize = remember { mutableStateOf(1)}
    val shapeList = remember { mutableStateListOf<Shape>()}
    val textList = remember { mutableStateListOf<TextBox>()}
    val textSelected = remember { mutableStateOf(false)}
    val rectangleSelected = remember { mutableStateOf(false)}
    val circleSelected = remember { mutableStateOf(false)}
    val triangleSelected = remember { mutableStateOf(false)}
    val currentText = remember {mutableStateOf("hello")}
    val deleteObjects = remember { mutableStateOf(false)}
    ToolSelection(sketchStatus, inUsedColor, brushSize, textSelected, currentText, rectangleSelected, circleSelected, triangleSelected, deleteObjects)

    // box acting as the real canvas, encompassing all composables and elements drawn
    Box(modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    if ( rectangleSelected.value ) {
                         val newRec = Rectangle(offset = tapOffset, color = inUsedColor.value, size = brushSize.value.dp)
                         shapeList.add(newRec)
                        rectangleSelected.value = false
                    }
                    if ( circleSelected.value ) {
                        val newCir = Circle(offset = tapOffset, color = inUsedColor.value, size = brushSize.value.dp)
                        shapeList.add(newCir)
                        circleSelected.value = false
                    }
                    if ( triangleSelected.value ) {
                        val newTri = Triangle(offset = tapOffset, color = inUsedColor.value, size = brushSize.value.dp)
                        shapeList.add(newTri)
                        triangleSelected.value = false
                    }
                    if (textSelected.value){
                        println(currentText.value)
                        val newText = TextBox(offset = tapOffset, currentText.value, color = inUsedColor.value, size = brushSize.value.dp)
                        textList.add(newText)
                        textSelected.value = false
                    }
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
                    if (sketchStatus.value) {
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
                    color = sketch.color,
                    start = sketch.start,
                    end = sketch.end,
                    strokeWidth = sketch.width.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        shapeList.forEach { shape -> shape.draw() }
        textList.forEach { text -> text.draw() }
        if (deleteObjects.value) {
            shapeList.removeLastOrNull()
            deleteObjects.value = false
        }
    }
}