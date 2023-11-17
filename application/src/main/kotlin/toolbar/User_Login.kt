package toolbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import data.User


@Composable
fun User_Login(user: User) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    user.updateUsername(text.text)
    Column(
        modifier = Modifier
    ) {
        Text("Enter User Login")
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            modifier = Modifier,
            value = text,
            onValueChange = {
                text = it
            },
            enabled = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null
                )
            },
            label = {
                Text(text = "Username"
                )
            },
            singleLine = true
        )
    }
}
