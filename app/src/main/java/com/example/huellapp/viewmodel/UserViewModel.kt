package com.example.huellapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huellapp.model.Usuario
import com.example.huellapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Estados para Login
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    // Estados para Registro
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
                val usuario = userRepository.login(email, password)
                if (usuario != null) {
                    _loginState.value = LoginUiState.Success(usuario)
                } else {
                    _loginState.value = LoginUiState.Error("Credenciales incorrectas")
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
                val usuario = Usuario(
                    id = 0,
                    nombre = nombre,
                    email = email,
                    password = password,
                    telefono = telefono
                )
                val success = userRepository.register(usuario)
                if (success) {
                    _registerState.value = RegisterUiState.Success(usuario)
                } else {
                    _registerState.value = RegisterUiState.Error("El usuario ya existe")
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

// ===== Estados para Login =====
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val usuario: Usuario) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

// ===== Estados para Registro =====
sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val usuario: Usuario) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}