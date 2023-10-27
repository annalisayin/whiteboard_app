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
    val openDialog = remember { mutableStateOf(false) }
    val buttonTitle = remember {
        mutableStateOf("Show Pop Up")
    }
    var selected by remember{ mutableStateOf(false)}
    val color = if (selected) androidx.compose.ui.graphics.Color.Green else Color.Gray

    Button(
        modifier = Modifier
            .padding(30.dp),
        onClick = {
            selected = !selected
            openDialog.value = !openDialog.value
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
        Text("A")
    }
    var text by remember { mutableStateOf(TextFieldValue()) }
    Box{
        if (openDialog.value) {
            buttonTitle.value = "Hide Pop Up"
            TextField(modifier = Modifier.align(Alignment.Center),
                value = text,
                onValueChange = {
                    text = it
                })
        }
    }
}