package canvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import toolbar.ToolSelection
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import data.*


@Composable
fun WhiteBoard() {
    val sketches = remember { mutableStateListOf<Sketch>() }
    val sketchStatus = remember { mutableStateOf(false) }
    val inUsedColor = remember { mutableStateOf(Color.Black)}
    val brushSize = remember { mutableStateOf(1)}
    val shapeList = remember { mutableStateListOf<Shape>()}
    val isInTextMode = remember { mutableStateOf(false)}
    val rectangleSelected = remember { mutableStateOf(false)}
    val circleSelected = remember { mutableStateOf(false)}
    val triangleSelected = remember { mutableStateOf(false)}
    var currentText = remember {mutableStateOf("hello")}
    ToolSelection(sketchStatus, inUsedColor, brushSize, isInTextMode, rectangleSelected, circleSelected, triangleSelected, currentText)

    // box acting as the real canvas, encompassing all composables and elements drawn
    Box(modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    if ( rectangleSelected.value ) {
                         val newRec = Rectangle(offset = tapOffset, color = inUsedColor.value)
                         shapeList.add(newRec)
                        rectangleSelected.value = false
                    }
                    if ( circleSelected.value ) {
                        val newCir = Circle(offset = tapOffset, color = inUsedColor.value)
                        shapeList.add(newCir)
                        circleSelected.value = false
                    }
                    if ( triangleSelected.value ) {
                        val newTri = Triangle(offset = tapOffset, color = inUsedColor.value)
                        shapeList.add(newTri)
                        triangleSelected.value = false
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
                        val sketch = Sketch(
                            start = change.position - dragAmount,
                            end = change.position,
                            color = inUsedColor.value,
                            width = brushSize.value.dp,
                        )
                        sketches.add(sketch)
                    }
                }

            }
        )
        {
            sketches.forEach { sketch ->
                drawLine(
                    color = sketch.color,
                    start = sketch.start,
                    end = sketch.end,
                    strokeWidth = sketch.width.toPx(),
                )
            }
        }
        shapeList.forEach { shape -> shape.draw() }
    }
}