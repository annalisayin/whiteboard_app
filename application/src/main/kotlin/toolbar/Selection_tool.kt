package toolbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Selection_tool() {
    val openDialog = remember { mutableStateOf(false) }
    val buttonTitle = remember {
        mutableStateOf("Show Pop Up")
    }

    Button(
        modifier = Modifier
            .padding(30.dp),
        onClick = {
            openDialog.value = !openDialog.value
        }
    ) {
        Icon(Icons.Filled.AddCircle, contentDescription = "Localized description")
    }
}