package data

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable

//@Serializable
//class TextBox(var offsetX: Int, var offsetY: Int, var curtext: String, var color: Int, var size: Int) {
//    @Composable
//    fun draw(){
//        SimpleFilledTextField(curtext, offsetX, offsetY, color, size)
//    }
//}
@Serializable
class TextBox(
    val offsetX: Int,
    val offsetY: Int,
    val curtext: String,
    val color: Int,
    val size: Int,
    val Id: Int? = -1,
){
    @Composable
    fun draw(){
        SimpleFilledTextField(curtext, offsetX, offsetY, color, size)
    }
}

@Composable
fun SimpleFilledTextField(curtext: String, offsetX: Int, offsetY: Int, color: Int, size: Int) {
    var text by remember { mutableStateOf(curtext) }
    val mutableOffsetX = remember {mutableStateOf(offsetX)}
    val mutableOffsetY = remember {mutableStateOf(offsetY)}
    Box(
        modifier = Modifier
            .offset(mutableOffsetX.value.dp, mutableOffsetY.value.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    mutableOffsetX.value += dragAmount.x.toInt() / 2
                    mutableOffsetY.value += dragAmount.y.toInt() / 2
                }
            }
            .padding(10.dp)
            .background(Color(color))
    ) {
        BasicTextField(
            modifier = Modifier
                .width(IntrinsicSize.Min),
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle(color = Color.Black, fontSize = (size*5).sp)
        )
    }

}