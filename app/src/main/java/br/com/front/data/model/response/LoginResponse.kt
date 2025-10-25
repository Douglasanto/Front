package br.com.front.data.model.response

data class LoginResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null,
    val user: UserResponse? = null,
)
