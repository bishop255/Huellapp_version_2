package com.example.huellapp.model

data class Dueno (
    val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val fotoDueño: String? = null,
    val perro: List<Perro> = emptyList()
)