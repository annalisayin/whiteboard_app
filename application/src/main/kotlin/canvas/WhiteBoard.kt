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
    val inUsedColor = remember { mutableStateOf(Color.Black)}
    val brushSize = remember { mutableStateOf(1)}
    val currentText = remember {mutableStateOf("hello")}

    val sketches = remember { mutableStateListOf<Sketch>() }
    val shapeList = remember { mutableStateListOf<Shape>()}
    val textList = remember { mutableStateListOf<TextBox>()}

    val currentTool = remember {mutableStateOf(-1)}
    val recentObject = remember { mutableStateListOf<Number>()}
    /* -1 = default (none)
        0 = pen
        1 = rectangle
        2 = circle
        3 = triangle
        4 = text */

    ToolSelection(currentTool, inUsedColor, brushSize, currentText)

    // box acting as the real canvas, encompassing all composables and elements drawn
    Box(modifier = Modifier
        .background(Color.Blue)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    if (currentTool.value == 1) {
                        val newRec = Rectangle(offset = tapOffset, color = inUsedColor.value)
                        shapeList.add(newRec)
                        currentTool.value = -1
                        recentObject.add(1)
                    }
                    if (currentTool.value == 2) {
                        val newCir = Circle(offset = tapOffset, color = inUsedColor.value)
                        shapeList.add(newCir)
                        currentTool.value = -1
                        recentObject.add(2)
                    }
                    if (currentTool.value == 3) {
                        val newTri = Triangle(offset = tapOffset, color = inUsedColor.value)
                        shapeList.add(newTri)
                        currentTool.value = -1
                        recentObject.add(3)
                    }
                    if (currentTool.value == 4){
                        println(currentText.value)
                        val newText = TextBox(offset = tapOffset, currentText.value)
                        textList.add(newText)
                        currentTool.value = -1
                        recentObject.add(4)
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
                    if (currentTool.value == 0) {
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
        if (currentTool.value == 5) {
            when(recentObject.lastOrNull()){
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
                1, 2, 3 -> {
                    shapeList.removeLastOrNull()
                    recentObject.removeLastOrNull()
                }
                4 -> {
                    textList.removeLastOrNull()
                    recentObject.removeLastOrNull()
                }
            }
            currentTool.value = -1
        }
    }
}