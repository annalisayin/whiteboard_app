package data

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.serialization.Serializable

@Serializable
class TextBox(private var curtext: String, var offsetX: Float, var offsetY: Float) {
    @Composable
    fun draw(){
        SimpleFilledTextField(curtext, offsetX, offsetY)
    }
}
@Composable
fun SimpleFilledTextField(curtext: String, offsetX: Float, offsetY: Float) {
    var text by remember { mutableStateOf(curtext) }
    val offset = Offset(offsetX, offsetY)
    var x = remember { mutableStateOf(offset.x.dp/2) }
    var y = remember { mutableStateOf(offset.y.dp/2) }
    Box(
        modifier = Modifier
            .offset(x.value, y.value)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    x.value += dragAmount.x.dp / 2
                    y.value += dragAmount.y.dp / 2
                }
            }
            .padding(10.dp)
    ) {
        BasicTextField(
            modifier = Modifier
                .width(IntrinsicSize.Min),
            value = text,
            onValueChange = { text = it },
        )
    }

}