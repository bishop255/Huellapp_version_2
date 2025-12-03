package com.example.huellapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.huellapp.model.Paseador
import com.example.huellapp.model.Paseo
import com.example.huellapp.repository.PaseoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaseoViewModel @Inject constructor(
    private val repository: PaseoRepository
) : ViewModel() {

    // Paseo en curso
    var paseoActivo = mutableStateOf<Paseo?>(null)
        private set

    // Historial (se mantiene en memoria con Compose)
    var historial = mutableStateListOf<Paseo>()
        private set

    // Inicia un paseo nuevo
    fun iniciarPaseo(paseador: Paseador, perro: String) {
        val codigo = "HX-${(1000..9999).random()}"

        paseoActivo.value = Paseo(
            codigo = codigo,
            paseador = paseador,
            perro = perro
        )
    }

    // Suma 1 segundo al paseo
    fun actualizarTiempo() {
        paseoActivo.value?.let {
            paseoActivo.value = it.copy(
                tiempoTranscurrido = it.tiempoTranscurrido + 1
            )
        }
    }

    // Finaliza paseo y lo guarda en el historial
    fun finalizarPaseo() {
        paseoActivo.value?.let { p ->
            val final = p.copy(finalizado = true)
            historial.add(final)
            paseoActivo.value = final
        }
    }
}

