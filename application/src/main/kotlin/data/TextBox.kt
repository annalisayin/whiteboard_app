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


class TextBox(var offset: Offset, private var curtext: String) {

    @Composable
    fun draw(){
        SimpleFilledTextField(curtext, offset)
    }
}
@Composable
fun SimpleFilledTextField(curtext: String, offset: Offset) {
    var text by remember { mutableStateOf(curtext) }
    var offsetX = remember { mutableStateOf(offset.x.dp/2) }
    var offsetY = remember { mutableStateOf(offset.y.dp/2) }
    Box(
        modifier = Modifier
            .offset(offsetX.value, offsetY.value)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX.value += dragAmount.x.dp / 2
                    offsetY.value += dragAmount.y.dp / 2
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