package data

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable

class Rectangle(override var x: Float, override var y: Float, override var color: Int, override var size: Int) : Shape()  {
    @Composable
    override fun draw() {
        RectangleComposable(color = color, x = x, y = y, size = size)
    }
}

@Composable
fun RectangleComposable(color: Int, x: Float, y: Float, size: Int){
   // Column(modifier = Modifier.offset(offset.x.dp, offset.y.dp)) {
    var offsetX = remember { mutableStateOf(x.dp/2) }
    var offsetY = remember { mutableStateOf(y.dp/2) }
    var size_m = size * 10
    Box(
            modifier = Modifier
                .offset(offsetX.value, offsetY.value)
                .size(size_m.dp).clip(RectangleShape)
                .background(Color.Black, RectangleShape)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX.value += dragAmount.x.dp/2
                        offsetY.value += dragAmount.y.dp/2
                    }
                }
    ) {}
    // }
}
