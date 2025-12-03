package com.example.huellapp.repository


import com.example.huellapp.model.remote.LoginRequest


import com.example.huellapp.api.ApiService

class AuthRepository(private val api: ApiService) {

    suspend fun login(email: String, password: String): com.example.huellapp.api.LoginResponse {
        return api.login(LoginRequest(email, password))
    }
}
