package br.com.front.data.api

import br.com.front.data.RegisterData
import br.com.front.data.model.request.UsuarioFullRequest
import br.com.front.data.model.request.LoginRequest
import br.com.front.data.model.request.UpdateUserRequest
import br.com.front.data.model.response.ApiResponse
import br.com.front.data.model.response.UserResponse
import br.com.front.data.model.response.UsuarioBackend
import br.com.front.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import br.com.front.data.model.nearby.NearbyPlaceResponse

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("usuarios/{id}")
    suspend fun getUser(@Path("id") userId: String): Response<ApiResponse<UserResponse>>

    @POST("auth/register-full")
    suspend fun registerUserFull(@Body userData: UsuarioFullRequest): Response<LoginResponse>

    @PUT("usuarios/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body body: UpdateUserRequest
    ): Response<UsuarioBackend>

    @GET("locais-proximos")
    suspend fun getNearbyPlaces(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("tipo") tipo: String? = null
    ): Response<List<NearbyPlaceResponse>>
}