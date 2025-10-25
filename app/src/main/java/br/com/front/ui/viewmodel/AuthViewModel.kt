package br.com.front.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.front.data.model.request.LoginRequest
import br.com.front.data.repository.UserRepository
import br.com.front.data.model.response.UserResponse
import br.com.front.data.session.UserSession
import br.com.front.data.session.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = UserRepository()
    private val store = UserDataStore(app)

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    init {
        viewModelScope.launch {
            store.loadOnce()?.let { UserSession.setUser(it) }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = repository.login(email, password)
                println("Response code: ${response.code()}")
                println("Response body: ${response.body()}")
                println("Response error: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true && responseBody.user != null) {
                        // Save user in session and propagate success state
                        UserSession.setUser(responseBody.user)
                        store.save(responseBody.user)
                        _loginState.value = LoginState.Success(responseBody.user)
                    } else {
                        val errorMsg = responseBody?.error ?: responseBody?.message ?: "Erro desconhecido"
                        println("API Error: $errorMsg")
                        _loginState.value = LoginState.Error(errorMsg)
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Erro HTTP: ${response.code()}"
                    println("HTTP Error: $errorBody")
                    _loginState.value = LoginState.Error(errorBody)
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
                e.printStackTrace()
                _loginState.value = LoginState.Error(e.message ?: "Erro de conexão")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            UserSession.clear()
            store.clear()
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: UserResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

