package toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import data.TextBox

@Composable
fun Text_tool(isInTextMode: MutableState<Boolean>, currentText: MutableState<String>) {
    var selected by remember{ mutableStateOf(false)}
    val color = if (selected) Color.Green else Color.Gray
    Button(
        modifier = Modifier
            .padding(30.dp),
        onClick = {
            selected = !selected
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
        Text("A")
    }
    var text by remember { mutableStateOf(TextFieldValue()) }
    Box{
        if (selected) {
            TextField(modifier = Modifier.align(Alignment.Center),
                value = text,
                onValueChange = {
                    text = it
                })
        }
    }
}