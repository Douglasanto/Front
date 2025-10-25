package br.com.front.data.repository

import br.com.front.data.api.RetrofitInstance
import br.com.front.data.model.request.LoginRequest
import br.com.front.data.model.request.UpdateUserRequest
import br.com.front.data.model.response.LoginResponse
import br.com.front.data.model.response.UserResponse
import br.com.front.data.model.response.UsuarioBackend
import retrofit2.Response

class UserRepository {
    private val api = RetrofitInstance.apiService

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }

    suspend fun updateUser(id: Long, body: UpdateUserRequest): Response<UsuarioBackend> = api.updateUser(id, body)

    suspend fun getUser(id: String) = api.getUser(id)
}