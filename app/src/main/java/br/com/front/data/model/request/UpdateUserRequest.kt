package br.com.front.data.model.request

data class UpdateUserRequest(
    val nome: String,
    val cpf: String,
    val email: String,
    val senha: String
)
