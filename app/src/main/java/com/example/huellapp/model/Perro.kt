package com.example.huellapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perro")
data class Perro(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val raza: String,
    val edad: Int,
    val peso: Double,
    val temperamento: String,
    val fotoUri: String? = null

)