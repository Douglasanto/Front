package br.com.front.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.front.data.model.request.UpdateUserRequest
import br.com.front.data.model.response.UserResponse
import br.com.front.data.model.response.UsuarioBackend
import br.com.front.data.repository.UserRepository
import br.com.front.data.session.UserSession
import br.com.front.data.session.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class ProfileViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _updateState = MutableStateFlow<UpdateUserState>(UpdateUserState.Idle)
    val updateState: StateFlow<UpdateUserState> = _updateState

    fun updateUser(id: Long, nome: String, cpf: String, email: String, senha: String, store: UserDataStore? = null) {
        viewModelScope.launch {
            _updateState.value = UpdateUserState.Loading
            try {
                val body = UpdateUserRequest(nome = nome, cpf = cpf, email = email, senha = senha)
                val response = repository.updateUser(id, body)
                Log.d("PROFILE", "updateUser code=${response.code()} body=${response.body()} error=${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    val backend: UsuarioBackend? = response.body()
                    if (backend != null) {
                        // Map backend entity to app-level UserResponse
                        val mapped = UserResponse(
                            id = backend.id_usuario,
                            email = backend.email,
                            nome = backend.nome,
                            token = null
                        )
                        try { store?.save(mapped) } catch (_: Exception) {}
                        _updateState.value = UpdateUserState.Success(mapped)
                    } else {
                        _updateState.value = UpdateUserState.Error("Resposta vazia do servidor")
                    }
                } else {
                    _updateState.value = UpdateUserState.Error("Erro HTTP: ${response.code()}")
                }
            } catch (e: Exception) {
                _updateState.value = UpdateUserState.Error(e.message ?: "Erro de conexão")
            }
        }
    }

    fun resetState() {
        _updateState.value = UpdateUserState.Idle
    }

    fun refreshUser(store: UserDataStore? = null) {
        viewModelScope.launch {
            val id = UserSession.user.value?.id ?: return@launch
            try {
                val resp = repository.getUser(id.toString())
                Log.d("PROFILE", "refreshUser code=${resp.code()} success=${resp.isSuccessful}")
                if (resp.isSuccessful) {
                    resp.body()?.data?.let {
                        UserSession.setUser(it)
                        try { store?.save(it) } catch (_: Exception) {}
                    }
                }
            } catch (_: Exception) { }
        }
    }
}

sealed class UpdateUserState {
    object Idle : UpdateUserState()
    object Loading : UpdateUserState()
    data class Success(val user: UserResponse) : UpdateUserState()
    data class Error(val message: String) : UpdateUserState()
}
