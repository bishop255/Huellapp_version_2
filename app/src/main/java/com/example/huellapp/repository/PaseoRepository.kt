package com.example.huellapp.repository

import com.example.huellapp.DAO.PaseoDao
import com.example.huellapp.model.Paseo
import com.example.huellapp.model.EstadoPaseo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaseoRepository @Inject constructor(
    private val dao: PaseoDao
) {

    fun obtenerHistorial(): Flow<List<Paseo>> = dao.obtenerHistorial()

    fun getAllPaseos(): Flow<List<Paseo>> = dao.getAllPaseos()

    fun getPaseosByEstado(estado: String): Flow<List<Paseo>> =
        dao.getPaseosByEstado(estado)

    fun getPaseosByPerro(perroId: Int): Flow<List<Paseo>> =
        dao.getPaseosByPerro(perroId)

    suspend fun guardarPaseo(
        codigo: String,
        paseadorId: Int,     // Cambiado de String a Int
        perroId: Int,        // Cambiado de String a Int
        duracion: Int,
        fecha: Long = System.currentTimeMillis(),
        hora: String = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            .format(java.util.Date()),
        estado: String = EstadoPaseo.PROGRAMADO.name
    ) {
        dao.insertPaseo(
            Paseo(
                codigo = codigo,
                paseadorId = paseadorId,  // Usa paseadorId
                perroId = perroId,        // Usa perroId
                duracionSegundos = duracion,
                fecha = fecha,
                hora = hora,
                estado = estado
            )
        )
    }

    suspend fun insertPaseo(paseo: Paseo) {
        dao.insertPaseo(paseo)
    }

    suspend fun deleteAllPaseos() {
        dao.deleteAllPaseos()
    }
}