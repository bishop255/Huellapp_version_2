package com.example.huellapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paseos")
data class Paseo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Cambiado de String a Int
    val codigo: String,
    val paseadorId: Int,
    val perroId: Int,
    val duracionSegundos: Int,
    val fecha: Long,
    val hora: String,
    val estado: String = EstadoPaseo.PROGRAMADO.name
)

enum class EstadoPaseo {
    PROGRAMADO, EN_CURSO, COMPLETADO, CANCELADO
}