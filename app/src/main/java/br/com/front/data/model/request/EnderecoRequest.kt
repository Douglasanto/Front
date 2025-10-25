package br.com.front.data.model.request

data class EnderecoRequest(
    val tipo_endereco: String? = null,
    val logradouro: String,
    val bairro: String,
    val cidade: String,
    val estado: String,
    val cep: String
)
