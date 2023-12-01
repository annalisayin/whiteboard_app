package data

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
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
data class TextBox(
    val offsetX: Int,
    val offsetY: Int,
    val curtext: String,
    val color: Int,
    val size: Int
)

@Composable
fun SimpleFilledTextField(curtext: String, offsetX: Int, offsetY: Int, color: Int, size: Int) {
    var text by remember { mutableStateOf(curtext) }
    val mutableOffsetX = remember {mutableStateOf(offsetX)}
    val mutableOffsetY = remember {mutableStateOf(offsetY)}
    Box(
        modifier = Modifier.offset(offsetX.dp, offsetY.dp)
    )
//    Box(
//        modifier = Modifier
//            .offset(mutableOffsetX.value.dp, mutableOffsetY.value.dp)
//            .pointerInput(Unit) {
//                detectDragGestures { change, dragAmount ->
//                    change.consume()
//                    mutableOffsetX.value += dragAmount.x.toInt() / 2
//                    mutableOffsetY.value += dragAmount.y.toInt() / 2
//                }
//            }
//            .padding(10.dp)
//    ) {
//        BasicTextField(
//            modifier = Modifier
//                .width(IntrinsicSize.Min),
//            value = text,
//            onValueChange = { text = it },
//            textStyle = TextStyle(color = Color(color), fontSize = (size*5).sp)
//        )
//    }

}