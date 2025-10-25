package br.com.front.data.model.response

// Alinha com a entidade Usuario do backend usada em UsuarioResource.update
// Incluímos apenas campos necessários para atualizar a sessão no app

data class UsuarioBackend(
    val id_usuario: Long,
    val nome: String,
    val email: String
)
