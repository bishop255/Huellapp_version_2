package com.example.huellapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huellapp.model.Perro
import com.example.huellapp.repository.PerroRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerroViewModel(private val repository: PerroRepository) : ViewModel() {

    // Flow que emite la lista de perros en tiempo real
    val perros = repository.perros
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Agregar nuevo perro
    fun agregarPerro(perro: Perro) {
        viewModelScope.launch {
            repository.agregarPerro(perro)
        }
    }

    // Eliminar perro específico
    fun eliminarPerro(perro: Perro) {
        viewModelScope.launch {
            repository.eliminarPerro(perro)
        }
    }

    // Eliminar todos los perros (opcional, útil para pruebas o reinicio)
    fun eliminarTodos() {
        viewModelScope.launch {
            repository.eliminarTodos()
        }
    }
}
