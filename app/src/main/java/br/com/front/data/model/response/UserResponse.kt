package br.com.front.data.model.response

data class UserResponse(
    val id: Long,
    val email: String,
    val nome: String,
    val token: String? = null,
    val endereco: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)