package data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String
)