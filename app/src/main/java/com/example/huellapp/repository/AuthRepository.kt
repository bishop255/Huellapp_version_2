package com.example.huellapp.repository

import com.example.huellapp.api.AuthApiService
import com.example.huellapp.model.remote.LoginRequest
import com.example.huellapp.model.remote.LoginResponse
import com.example.huellapp.model.remote.RegisterRequest
import com.example.huellapp.model.remote.RegisterResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApiService
) {

    suspend fun login(email: String, password: String): LoginResponse {
        val request = LoginRequest(email, password)

        return try {
            api.login(request)
        } catch (e: HttpException) {
            val message = when (e.code()) {
                401, 403 -> "Credenciales inválidas"
                500 -> "Error del servidor"
                else -> "Error HTTP: ${e.code()}"
            }
            throw Exception(message)
        } catch (e: IOException) {
            throw Exception("Error de conexión. Verifica tu internet o que el servidor esté corriendo.")
        } catch (e: Exception) {
            throw Exception("Error desconocido: ${e.localizedMessage}")
        }
    }

    suspend fun register(request: RegisterRequest): RegisterResponse {
        return try {
            api.register(request)
        } catch (e: HttpException) {
            val message = when (e.code()) {
                400 -> "Datos inválidos"
                409 -> "El usuario ya existe"
                else -> "Error HTTP: ${e.code()}"
            }
            throw Exception(message)
        } catch (e: IOException) {
            throw Exception("Error de conexión")
        } catch (e: Exception) {
            throw Exception("Error: ${e.localizedMessage}")
        }
    }
}