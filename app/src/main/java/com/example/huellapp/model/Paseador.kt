package com.example.huellapp.model

data class Paseador(
    val id: String,
    val nombre: String,
    val calificacion: Float,
    val experiencia: Int,
    val tarifa: Int,
    val disponible: Boolean
)