package com.example.huellapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huellapp.model.Usuario
import com.example.huellapp.model.remote.RegisterRequest
import com.example.huellapp.repository.AuthRepository  // CAMBIADO
import com.example.huellapp.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository  // USA AuthRepository en lugar de UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginUiState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            try {
                val response = authRepository.login(email, password)  // LLAMADA AL BACKEND

                if (response.estado == "exito" && response.usuario != null) {
                    // Guardar en sesión
                    val usuario = Usuario(
                        id = 0,
                        nombre = response.usuario.nombre,
                        email = response.usuario.email,
                        password = "",
                        telefono = ""
                    )
                    UserSession.usuarioLogueado = usuario

                    _loginState.value = LoginUiState.Success(usuario)
                } else {
                    _loginState.value = LoginUiState.Error(response.mensaje ?: "Credenciales incorrectas")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun register(nombre: String, email: String, password: String, telefono: String) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = RegisterUiState.Error("Por favor completa todos los campos")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registerState.value = RegisterUiState.Error("Correo electrónico inválido")
            return
        }

        if (password.length < 6) {
            _registerState.value = RegisterUiState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _registerState.value = RegisterUiState.Loading
            try {
                val request = RegisterRequest(
                    email = email,
                    password = password,
                    nombre = nombre,
                    telefono = telefono,
                    direccionPrincipal = null,
                    preferencias = null,
                    contactoEmergencia = null
                )

                val response = authRepository.register(request)  // LLAMADA AL BACKEND

                if (response.estado == "exito") {
                    val usuario = Usuario(
                        id = 0,
                        nombre = nombre,
                        email = email,
                        password = "",
                        telefono = telefono
                    )
                    _registerState.value = RegisterUiState.Success(usuario)
                } else {
                    _registerState.value = RegisterUiState.Error(response.mensaje)
                }
            } catch (e: Exception) {
                _registerState.value = RegisterUiState.Error(e.message ?: "Error al registrar")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginUiState.Idle
    }

    fun resetRegisterState() {
        _registerState.value = RegisterUiState.Idle
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val usuario: Usuario) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val usuario: Usuario) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}