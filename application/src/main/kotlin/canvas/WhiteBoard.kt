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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import data.*
import toolbar.ToolSelection


@Composable
fun WhiteBoard() {
    val sketches = remember { mutableStateListOf<Sketch>() }
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
                         val newRec = Rectangle(offset = tapOffset, color = Color(inUsedColor.value))
                         shapeList.add(newRec)
                        rectangleSelected.value = false
                    }
                    if ( circleSelected.value ) {
                        val newCir = Circle(offset = tapOffset, color = Color(inUsedColor.value))
                        shapeList.add(newCir)
                        circleSelected.value = false
                    }
                    if ( triangleSelected.value ) {
                        val newTri = Triangle(offset = tapOffset, color = Color(inUsedColor.value))
                        shapeList.add(newTri)
                        triangleSelected.value = false
                    }
                    if (textSelected.value){
                        println(currentText.value)
                        val newText = TextBox(offset = tapOffset, currentText.value)
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
                detectDragGestures { change, dragAmount ->
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
                        sketches.add(sketch)
                    }
                }

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