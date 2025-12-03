package com.example.huellapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huellapp.session.UserSession
import com.example.huellapp.model.Usuario
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val usuario: Usuario) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val state: StateFlow<LoginUiState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginUiState.Loading

            delay(1000)

            if (email.isNotEmpty() && password.isNotEmpty()) {

                val usuario = Usuario(
                    id = 1,
                    nombre = "Pedro", // temporal
                    correo = email
                )

                UserSession.usuarioLogueado = usuario

                _state.value = LoginUiState.Success(usuario)
            } else {
                _state.value = LoginUiState.Error("Credenciales inválidas")
            }
        }
    }

}
