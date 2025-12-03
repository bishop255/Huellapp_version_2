package com.example.huellapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paseos")
data class Paseo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: String,
    val paseadorId: Int,
    val perroId: Int,
    val duracionSegundos: Int,
    val fecha: String,
    val hora: String,
    val estado: String = EstadoPaseo.PROGRAMADO.name
)


enum class EstadoPaseo {
    PROGRAMADO, EN_CURSO, COMPLETADO, CANCELADO
}
