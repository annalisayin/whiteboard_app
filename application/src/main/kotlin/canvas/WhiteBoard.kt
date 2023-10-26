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
    val textBoxesOnCanvas = remember { mutableStateListOf<TextBox>() }
    var isInTextMode = remember { mutableStateOf(false)}
    var currentText = remember {mutableStateOf("hello")}
    val textMeasurer = rememberTextMeasurer()
    ToolSelection(sketchStatus, inUsedColor, brushSize, shapesOnCanvas = shapesOnCanvas, isInTextMode, currentText)
    Box(modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize() , contentAlignment = Alignment.Center
    ) {
        Rectangle().onDraw()
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        println(isInTextMode.value)
                        println(tapOffset.x.toString() + " " + tapOffset.y.toString())
                        if (isInTextMode.value) {
                            val newTextBox = TextBox(currentText.value, tapOffset)
                            textBoxesOnCanvas.add(newTextBox)
                            println(textBoxesOnCanvas[textBoxesOnCanvas.size - 1])
                        }
                    }
                )
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
            textBoxesOnCanvas.forEach { textBox ->
                drawText(textMeasurer, textBox.text, topLeft = textBox.offset)
            }
        }
    }
}