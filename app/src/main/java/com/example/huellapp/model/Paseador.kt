package com.example.huellapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paseadores")
data class Paseador(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val calificacion: Float,
    val experiencia: Int,
    val tarifa: Int,
    val disponible: Boolean = true
)