package com.example.huellapp.api

import com.example.huellapp.model.remote.LoginRequest
import com.example.huellapp.model.remote.LoginResponse
import com.example.huellapp.model.remote.RegisterRequest
import com.example.huellapp.model.remote.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("login")  // SIN barra inicial
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("usuarios")  // SIN barra inicial
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}