package toolbar
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.material.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.PopupProperties
import data.Shape

import data.Rectangle
import data.TriangleShape

@Composable
fun Shapes_tool(currentTool: MutableState<Int>) {

    val openDialog = remember { mutableStateOf(false) }
    val buttonTitle = remember {
        mutableStateOf("Show Pop Up")
    }
    val color = if (currentTool.value in 1 ..3) Color.Green else Color.Gray
    val colorRec = if (currentTool.value == 1) Color.Green else Color.Gray
    val colorCircle = if (currentTool.value == 2) Color.Green else Color.Gray
    val colorTriangle = if (currentTool.value == 3) Color.Green else Color.Gray

    // Define a variable to track the selected shape type
    Button(
        modifier = Modifier
            .padding(10.dp)
            .height(50.dp),
        onClick = {
            openDialog.value = !openDialog.value
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        ) {
        when(currentTool.value){
            1 -> Box(modifier = Modifier.size(17.dp).background(Color.Black))
            2 -> Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Color.Black))
            3 -> Box(modifier = Modifier.size(20.dp).clip(TriangleShape).background(Color.Black))
            else -> Icon(Icons.Filled.Star, contentDescription = "Localized description")
        }

    }
    Box {
        if (openDialog.value) {
            buttonTitle.value = "Hide Pop Up"
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties()
            ) {
                Box(
                    Modifier
                        .padding(top = 5.dp)
                        .background(Color.LightGray, RoundedCornerShape(10.dp))
                        .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Column {
                            Button(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = {
                                    currentTool.value = if (currentTool.value == 1) -1 else 1
                                    openDialog.value = !openDialog.value
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = colorRec)
                            ) {
                                Box(modifier = Modifier.size(17.dp).background(Color.Black))
                            }
                            Button(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = {
                                    currentTool.value = if (currentTool.value == 2) -1 else 2
                                    openDialog.value = !openDialog.value
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = colorCircle)
                            ) {
                                Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Color.Black)) }
                            Button(
                                modifier = Modifier.padding(10.dp),
                                onClick = {
                                    currentTool.value = if (currentTool.value == 3) -1 else 3
                                    openDialog.value = !openDialog.value
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = colorTriangle)
                            ) {
                                Box(modifier = Modifier.size(20.dp).clip(TriangleShape).background(Color.Black))
                            }
                        }
                        // Add buttons for other shape types if needed
                    }
                }
            }
        }
    }
}

