package toolbar


import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Delete_tool(inDelete: MutableState<Boolean>, currentTool: MutableState<Int>) {
    val openDialog = remember { mutableStateOf(false) }
    val color = if (inDelete.value == true) Color.Green else Color.Gray
    val buttonTitle = remember {
        mutableStateOf("Show Pop Up")
    }
    Button(
        modifier = Modifier
            .padding(30.dp),
        onClick = {
            currentTool.value = if (currentTool.value == 5) -1 else 5
            inDelete.value = if (inDelete.value == true) false else true
            println("InDelete is: $inDelete")
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
        Icon(Icons.Filled.Delete, contentDescription = "Localized description")
    }
}