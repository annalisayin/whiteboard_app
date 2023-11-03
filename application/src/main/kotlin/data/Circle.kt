package data

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Circle(offset: Offset) : Shape(offset) {
    var size = 100.dp
    @Composable
    override fun draw() {
        CircleComposable(color = color, offset = offset, size = size)
    }
}

@Composable
fun CircleComposable(color: Color, offset: Offset, size: Dp){
    // Column(modifier = Modifier.offset(offset.x.dp, offset.y.dp)) {
    var offsetX = remember { mutableStateOf(offset.x.dp/2) }
    var offsetY = remember { mutableStateOf(offset.y.dp/2) }
    var size_m = size
    println(offset.x.toString() + " " + offset.y.toString())
    Box(
        modifier = Modifier
            .offset(offsetX.value, offsetY.value)
            .size(size_m).clip(CircleShape)
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