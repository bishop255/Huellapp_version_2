package com.example.huellapp.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "perro")
data class Perro(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val raza: String,
    val edad: Int,
    val peso: Float,
    val temperamento: String,
    val fotoUri: String? = null

)