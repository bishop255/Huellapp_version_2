package com.example.huellapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huellapp.model.EstadoPaseo
import com.example.huellapp.model.Paseo
import com.example.huellapp.repository.PaseoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PaseoViewModel @Inject constructor(
    private val repository: PaseoRepository
) : ViewModel() {


    private val _allPaseos = repository.getAllPaseos()


    val paseosActivos: Flow<List<Paseo>> = _allPaseos.map { paseos ->
        paseos.filter { it.estado == EstadoPaseo.EN_CURSO.name }
    }


    val historial: Flow<List<Paseo>> = _allPaseos.map { paseos ->
        paseos.filter { it.estado == EstadoPaseo.COMPLETADO.name }
    }


    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun iniciarPaseo(
        paseadorId: Int,
        perroId: Int,
        perroNombre: String,
        duracionMinutos: Int = 60
    ) {
        viewModelScope.launch {
            try {
                val codigo = generarCodigoPaseo()
                val ahora = System.currentTimeMillis()
                val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(ahora))

                repository.guardarPaseo(
                    codigo = codigo,
                    paseadorId = paseadorId,
                    perroId = perroId,
                    duracion = duracionMinutos * 60,
                    fecha = ahora,
                    hora = horaActual,
                    estado = EstadoPaseo.EN_CURSO.name
                )

                _mensaje.value = "Paseo iniciado: $codigo"
            } catch (e: Exception) {
                _mensaje.value = "Error al iniciar paseo: ${e.message}"
            }
        }
    }

    fun finalizarPaseo(paseoId: Int) {
        viewModelScope.launch {
            try {
                // Usa el nuevo método del repository
                repository.actualizarEstadoPaseo(paseoId, EstadoPaseo.COMPLETADO.name)
                _mensaje.value = "Paseo finalizado exitosamente"
            } catch (e: Exception) {
                _mensaje.value = "Error al finalizar paseo: ${e.message}"
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    private fun generarCodigoPaseo(): String {
        val timestamp = System.currentTimeMillis()
        return "P${timestamp.toString().takeLast(6)}"
    }
}