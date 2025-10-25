package br.com.front.data.repository

import br.com.front.data.RegisterData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

class AuthRepository(private val authApi: AuthApiService) {
    interface AuthApiService {
        @POST("auth/register")
        suspend fun register(@Body registerData: RegisterData): Response<Void>
    }

    suspend fun register(registerData: RegisterData) = authApi.register(registerData)
}
