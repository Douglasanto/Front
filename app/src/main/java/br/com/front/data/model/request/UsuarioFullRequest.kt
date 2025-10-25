package br.com.front.data.model.request

data class UsuarioFullRequest(
    val nome: String,
    val nome_social: String? = null,
    val cpf: String,
    val data_nascimento: String? = null, // dd/MM/yyyy
    val email: String,
    val senha: String,
    val telefone: Long? = null,
    val telefone_contato: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val plusCode: String? = null,
    // enums as backend expects (lowercase names defined in backend)
    val cor: String? = null,
    val escolaridade: String? = null,
    val renda_mensal: String? = null,
    val sexo: String? = null,
    val tipo_moradia: String? = null,
    val estado_civil: String? = null,
    // associations
    val deficienciaIds: List<Long>? = null,
    val bens: List<BeneficioRequest>? = null,
    val profs: List<ProfissaoRequest>? = null,
    // addresses
    val enderecos: List<EnderecoRequest>? = null
)
