package data

import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset

data class TextBox(
    val text: String,
    val offset: Offset,
)
@Composable
fun SimpleFilledTextField() {
    var text by remember { mutableStateOf("Hello") }

    TextField(
        value = text,
        onValueChange = { text = it },
    )
}