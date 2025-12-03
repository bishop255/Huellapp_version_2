package com.example.huellapp.model

data class Calificacion(
    val id: String = "",
    val PaseoId: String = "",
    val CalificadorId: String = "",
    val CalificadoId: String = "",
    val puntuacion: Double = 0.0,
    val comentario: String = "",
    val fecha: String = ""
)
