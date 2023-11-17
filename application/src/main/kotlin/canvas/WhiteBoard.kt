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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import data.*
import toolbar.ToolSelection


@Composable
fun WhiteBoard() {
    val sketches = remember { mutableStateListOf<Sketch>() }
    val sketchStatus = remember { mutableStateOf(false) }
    val inUsedColor = remember { mutableStateOf(Color.Black)}
    val brushSize = remember { mutableStateOf(1)}
    val shapeList = remember { mutableStateListOf<Shape>()}
    val textList = remember { mutableStateListOf<TextBox>()}
    val textSelected = remember { mutableStateOf(false)}
    val rectangleSelected = remember { mutableStateOf(false)}
    val circleSelected = remember { mutableStateOf(false)}
    val triangleSelected = remember { mutableStateOf(false)}
    val currentText = remember {mutableStateOf("hello")}
    val undoSelected = remember { mutableStateOf(false)}
    val recentObject = remember { mutableStateListOf<Number>()}
    /*
        -1 = default (none)
        0 = pen
        1 = shape
        2 = text
     */
    ToolSelection(sketchStatus, inUsedColor, brushSize, textSelected, currentText, rectangleSelected, circleSelected, triangleSelected, undoSelected)

    // box acting as the real canvas, encompassing all composables and elements drawn
    Box(modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    if (rectangleSelected.value) {
                        val newRec = Rectangle(offset = tapOffset, color = inUsedColor.value)
                        shapeList.add(newRec)
                        rectangleSelected.value = false
                        recentObject.add(1)
                    }
                    if (circleSelected.value) {
                        val newCir = Circle(offset = tapOffset, color = inUsedColor.value)
                        shapeList.add(newCir)
                        circleSelected.value = false
                        recentObject.add(1)
                    }
                    if (triangleSelected.value) {
                        val newTri = Triangle(offset = tapOffset, color = inUsedColor.value)
                        shapeList.add(newTri)
                        triangleSelected.value = false
                        recentObject.add(1)
                    }
                    if (textSelected.value){
                        println(currentText.value)
                        val newText = TextBox(offset = tapOffset, currentText.value)
                        textList.add(newText)
                        textSelected.value = false
                        recentObject.add(2)
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
                        recentObject.add(0)
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
        textList.forEach { text -> text.draw() }
        if (undoSelected.value) {
            when(recentObject.last()){
                0 -> {
                    for(i in sketches.indices.reversed()){
                        if(i > 0 && sketches[i].start == sketches[i-1].end){
                            sketches.removeLastOrNull()
                            recentObject.removeLastOrNull()
                        } else{
                            break;
                        }
                    }
                    sketches.removeLastOrNull()
                    recentObject.removeLastOrNull()
                }
                1 -> {
                    shapeList.removeLastOrNull()
                    recentObject.removeLastOrNull()
                }
                2 -> {
                    textList.removeLastOrNull()
                    recentObject.removeLastOrNull()
                }
            }
            undoSelected.value = false
        }
    }
}