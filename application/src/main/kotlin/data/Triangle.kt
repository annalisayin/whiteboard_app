package data

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Triangle(offset: Offset, color: Color, size: Dp) : Shape(offset, color, size) {
    @Composable
    override fun draw() {
        TriangleComposable(color = color, offset = offset, size = size)
    }
}

private val TriangleShape = GenericShape { size, _ ->
    // 1)
    moveTo(size.width / 2f, 0f)

    // 2)
    lineTo(size.width, size.height)

    // 3)
    lineTo(0f, size.height)
}
@Composable
fun TriangleComposable(color: Color, offset: Offset, size: Dp){
    // Column(modifier = Modifier.offset(offset.x.dp, offset.y.dp)) {
    var offsetX = remember { mutableStateOf(offset.x.dp/2) }
    var offsetY = remember { mutableStateOf(offset.y.dp/2) }
    var size_m = size * 10
    println(offset.x.toString() + " " + offset.y.toString())
    Box(
        modifier = Modifier
            .offset(offsetX.value, offsetY.value)
            .size(size_m).clip(TriangleShape)
            .background(color)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX.value += dragAmount.x.dp/2
                    offsetY.value += dragAmount.y.dp/2
                }
            }
    )
    // }
}