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
import data.ShapeType
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import data.Shape
import data.TextBox


@Composable
fun WhiteBoard() {
    val sketches = remember { mutableStateListOf<Sketch>() }
    val sketchStatus = remember { mutableStateOf(false) }
    val inUsedColor = remember { mutableStateOf(Color.Black)}
    val brushSize = remember { mutableStateOf(1)}
    val shapesOnCanvas by remember { mutableStateOf(mutableStateListOf<Shape>()) }
    val textBoxesOnCanvas = remember { mutableStateListOf<TextBox>() }
    var isInTextMode = remember { mutableStateOf(false)}
    var currentText = remember {mutableStateOf("hello")}
    val textMeasurer = rememberTextMeasurer()
    ToolSelection(sketchStatus, inUsedColor, brushSize, shapesOnCanvas = shapesOnCanvas, isInTextMode, currentText)
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
            detectTransformGestures { _, pan, _, _ ->
                shapesOnCanvas.forEach { shape ->
                        val updatedPosition = Offset(
                            shape.position.value.x + pan.x,
                            shape.position.value.y + pan.y
                        )
                        shape.position.value = updatedPosition // Update the position directly
                }
            }

        }
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    println(isInTextMode.value)
                    println(tapOffset.x.toString() + " " + tapOffset.y.toString())
                    if ( isInTextMode.value ) {
                        val newTextBox = TextBox(currentText.value, tapOffset)
                        textBoxesOnCanvas.add(newTextBox)
                        println(textBoxesOnCanvas[textBoxesOnCanvas.size-1])
                    }
                }
            )
        }
    )
    {
        sketches.forEach{sketch -> drawLine(
            color = sketch.color,
            start = sketch.start,
            end = sketch.end,
            strokeWidth = sketch.width.toPx(),
        )}
        textBoxesOnCanvas.forEach { textBox ->
            drawText(textMeasurer, textBox.text, topLeft = textBox.offset)
        }
        shapesOnCanvas.forEach { shape ->
            when (shape.type) {
                ShapeType.Rectangle -> {
                    val rect = shape.rect
                    if (rect != null) {
                        drawRect(
                            color = shape.color,
                            style = Stroke(width = 2.dp.toPx()),
                            topLeft = shape.position.value,
                            size = Size(rect.width, rect.height)
                        )
                    }
                }
                ShapeType.Circle -> {
                    val circle = shape.circle
                    if (circle != null) {
                        drawCircle(
                            color = shape.color,
                            style = Stroke(width = 2.dp.toPx()),
                            center = Offset(circle.centerX, circle.centerY),
                            radius = circle.radius
                        )
                    }
                }

                ShapeType.Triangle -> {
                    val triangle = shape.triangle
                    if (triangle != null) {
                        drawPath(
                            color = shape.color,
                            style = Stroke(width = 2.dp.toPx()),
                            path = Path().apply {
                                moveTo(triangle.x1, triangle.y1)
                                lineTo(triangle.x2, triangle.y2)
                                lineTo(triangle.x3, triangle.y3)
                                close()
                            }
                        )
                    }
                }
                // Handle other shape types (e.g., Triangle) here
            }
        }
    }
}