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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.window.PopupProperties
import data.Shape

import data.Rectangle


@Composable
fun Shapes_tool(rectangleSelected: MutableState<Boolean>, circleSelected: MutableState<Boolean>, triangleSelected: MutableState<Boolean>) {

    val openDialog = remember { mutableStateOf(false) }
    val buttonTitle = remember {
        mutableStateOf("Show Pop Up")
    }
    // Define a variable to track the selected shape type
    Button(
        modifier = Modifier
            .padding(10.dp),
        onClick = {
            openDialog.value = !openDialog.value
        }
    ) {
        Icon(Icons.Filled.Star, contentDescription = "Localized description")
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
                        .background(Color.Gray, RoundedCornerShape(10.dp))
                        .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Column {
                            Button(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = {
                                    rectangleSelected.value = !rectangleSelected.value
                                    openDialog.value = !openDialog.value
                                }
                            ) {
                                Icon(Icons.Default.MailOutline, contentDescription = "Create Rectangle")
                            }
                            Button(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = {
                                    circleSelected.value = !circleSelected.value
                                    openDialog.value = !openDialog.value
                                }
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Create Circle")
                            }
                            Button(
                                modifier = Modifier.padding(10.dp),
                                onClick = {
                                    triangleSelected.value = !triangleSelected.value
                                    openDialog.value = !openDialog.value
                                }
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Create Triangle")
                            }
                        }
                        // Add buttons for other shape types if needed
                    }
                }
            }
        }
    }
}

