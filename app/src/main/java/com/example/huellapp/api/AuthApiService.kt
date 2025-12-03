package com.example.huellapp.api

import com.example.huellapp.model.remote.LoginRequest
import com.example.huellapp.model.remote.LoginResponse
import com.example.huellapp.model.remote.RegisterRequest
import com.example.huellapp.model.remote.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("usuarios")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}
