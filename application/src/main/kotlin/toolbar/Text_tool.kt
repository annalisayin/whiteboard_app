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
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import data.TextBox

@Composable
fun Text_tool(textSelected: MutableState<Boolean>, currentText: MutableState<String>) {
    //val openDialog = remember { mutableStateOf(false) }
    val buttonTitle = remember {
        mutableStateOf("Show Pop Up")
    }
    //val color = if (textSelected.value) Color.Green else Color.Gray
    var text by remember { mutableStateOf(TextFieldValue()) }
//    isInTextMode.value = openDialog.value
    currentText.value = text.text

    Button(
        modifier = Modifier
            .padding(30.dp),
        onClick = {
            textSelected.value = !textSelected.value
        },
    ) {
        Text("A", fontSize = 20.sp)
    }
    Box{
        if (textSelected.value) {
            buttonTitle.value = "Hide Pop Up"
            TextField(modifier = Modifier
                //.align(Alignment.Center)
                .width(width=150.dp),
                value = text,
                enabled = true,
                onValueChange = {
                    text = it
                })
        }
    }
}