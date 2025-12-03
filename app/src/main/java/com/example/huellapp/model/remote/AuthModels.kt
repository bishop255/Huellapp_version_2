package com.example.huellapp.model.remote

// LOGIN
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

// REGISTRO
data class RegisterRequest(
    val email: String,
    val password: String,
    val nombre: String,
    val telefono: String,
    val direccionPrincipal: String?,
    val preferencias: String?,
    val contactoEmergencia: String?
)

data class RegisterResponse(
    val estado: String,
    val mensaje: String
)