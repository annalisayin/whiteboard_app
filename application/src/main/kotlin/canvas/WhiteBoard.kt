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
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import data.Rectangle
import data.Shape
import data.TextBox


@Composable
fun WhiteBoard() {
    val sketches = remember { mutableStateListOf<Sketch>() }
    val sketchStatus = remember { mutableStateOf(false) }
    val inUsedColor = remember { mutableStateOf(Color.Black)}
    val brushSize = remember { mutableStateOf(1)}
    val shapesOnCanvas by remember { mutableStateOf(mutableStateListOf<Shape>()) }
    val shapeList = remember { mutableStateListOf<Shape>()}
    val isInTextMode = remember { mutableStateOf(false)}
    var currentText = remember {mutableStateOf("hello")}
    ToolSelection(sketchStatus, inUsedColor, brushSize, shapesOnCanvas = shapesOnCanvas, isInTextMode, currentText)

    // box acting as the real canvas, encompassing all composables and elements drawn
    Box(modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    val newRec = Rectangle(offset = tapOffset)
                    shapeList.add(newRec)
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