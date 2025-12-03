package com.example.huellapp.api

import com.example.huellapp.model.remote.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val estado: String,
    val mensaje: String,
    val usuario: UsuarioBackend?
)

data class UsuarioBackend(
    val nombre: String,
    val email: String
)

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
