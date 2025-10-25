package br.com.front.data.session

import br.com.front.data.model.response.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserSession {
    private val _user: MutableStateFlow<UserResponse?> = MutableStateFlow(null)
    val user: StateFlow<UserResponse?> = _user

    fun setUser(userResponse: UserResponse?) {
        _user.value = userResponse
    }

    fun clear() {
        _user.value = null
    }
}
