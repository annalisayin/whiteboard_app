package data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class User() {
    var username by mutableStateOf("")
        private set

    fun updateUsername(input: String) {
        username = input
    }
}