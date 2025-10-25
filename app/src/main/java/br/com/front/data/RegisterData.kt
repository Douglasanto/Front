package br.com.front.data

import kotlin.String
import com.google.gson.Gson
import android.util.Log

data class RegisterData(
    // Basic Info
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val senha: String = "",
    
    // Personal Info
    val cpf: String = "",
    val data_nascimento: String? = null,
    val sexo: String? = null,
    val nome_social: String? = null,
    
    // Address
    val cep: String = "",
    val logradouro: String = "",
    val numero: String = "",
    val complemento: String = "",
    val bairro: String = "",
    val cidade: String = "",
    val estado: String = "",
    val tipo_moradia: String? = null,
    val tipoTransporte: String? = null,
    
    // Socioeconomic
    val escolaridade: String? = null,
    val renda_mensal: Double? = null,
    val estado_civil: String? = null,
    val ocupacao: String? = null,
    val participaPrograma: String? = null,
    val programa: String? = null,
    val nis: String? = null,
    val renda: String = "",
    
    // Disabilities and other fields
    val defs: List<String> = emptyList(),
    val bens: List<Any> = emptyList(),
    val end: List<Any> = emptyList(),
    val profs: List<Any> = emptyList(),
    
    // System fields (will be set by backend)
    val id_usuario: Long = 0,
    val perfis: List<Perfil> = listOf(Perfil(2, "ROLE_USER")),
    val enabled: Boolean = true,
    val username: String = "",
    val password: String = "",
    val authorities: List<Authority> = listOf(Authority("ROLE_USER")),
    val accountNonExpired: Boolean = true,
    val accountNonLocked: Boolean = true,
    val credentialsNonExpired: Boolean = true,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val plusCode: String? = null,
    val cor: String? = null
) {
    data class Perfil(
        val id: Int,
        val nome: String
    )
    
    data class Authority(
        val authority: String
    )
    fun copyWithUpdates(
        nome: String = this.nome,
        email: String = this.email,
        telefone: String = this.telefone,
        senha: String = this.senha,
        cpf: String = this.cpf,
        data_nascimento: String? = this.data_nascimento,
        sexo: String? = this.sexo,
        nome_social: String? = this.nome_social,
        cep: String = this.cep,
        logradouro: String = this.logradouro,
        numero: String = this.numero,
        complemento: String = this.complemento,
        bairro: String = this.bairro,
        cidade: String = this.cidade,
        estado: String = this.estado,
        tipo_moradia: String? = this.tipo_moradia,
        tipoTransporte: String? = this.tipoTransporte,
        escolaridade: String? = this.escolaridade,
        renda_mensal: Double? = this.renda_mensal,
        estado_civil: String? = this.estado_civil,
        defs: List<String> = this.defs,
        bens: List<Any> = this.bens,
        end: List<Any> = this.end,
        profs: List<Any> = this.profs,
        id_usuario: Long = this.id_usuario,
        perfis: List<Perfil> = this.perfis,
        enabled: Boolean = this.enabled,
        username: String = this.username,
        password: String = this.password,
        authorities: List<Authority> = this.authorities,
        accountNonExpired: Boolean = this.accountNonExpired,
        accountNonLocked: Boolean = this.accountNonLocked,
        credentialsNonExpired: Boolean = this.credentialsNonExpired,
        latitude: Double = this.latitude,
        longitude: Double = this.longitude,
        plusCode: String? = this.plusCode,
        cor: String? = this.cor,
        ocupacao: String,
        participaPrograma: String,
        programa: String,
        nis: String
    ) = copy(
        nome = nome,
        email = email,
        telefone = telefone,
        senha = senha,
        cpf = cpf,
        data_nascimento = data_nascimento,
        sexo = sexo,
        nome_social = nome_social,
        cep = cep,
        logradouro = logradouro,
        numero = numero,
        complemento = complemento,
        bairro = bairro,
        cidade = cidade,
        estado = estado,
        tipo_moradia = tipo_moradia,
        escolaridade = escolaridade,
        renda_mensal = renda_mensal,
        estado_civil = estado_civil,
        defs = defs,
        bens = bens,
        end = end,
        profs = profs,
        id_usuario = id_usuario,
        perfis = perfis,
        enabled = enabled,
        username = username,
        password = password,
        authorities = authorities,
        accountNonExpired = accountNonExpired,
        accountNonLocked = accountNonLocked,
        credentialsNonExpired = credentialsNonExpired,
        latitude = latitude,
        longitude = longitude,
        plusCode = plusCode,
        cor = cor
    )
}

fun RegisterData.toJsonSafe(): String {
    return try {
        Gson().toJson(this)
    } catch (e: Exception) {
        Log.e("RegisterData", "Erro ao converter para JSON: ${e.message}")
        "{}"
    }
}
