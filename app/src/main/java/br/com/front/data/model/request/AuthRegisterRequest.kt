package br.com.front.data.model.request

data class AuthRegisterRequest(
    val nome: String,
    val email: String,
    val senha: String,
    val cpf: String,
    val telefone: Long,
    val username: String
)
