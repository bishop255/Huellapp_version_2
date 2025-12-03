package com.example.huellapp.repository

import com.example.huellapp.DAO.PerroDao
import com.example.huellapp.model.Perro
import kotlinx.coroutines.flow.Flow

class PerroRepository(private val perroDao: PerroDao) {

    val perros: Flow<List<Perro>> = perroDao.obtenerPerros()

    suspend fun agregarPerro(perro: Perro) {
        perroDao.insertarPerro(perro)
    }

    suspend fun eliminarPerro(perro: Perro) {
        perroDao.eliminarPerro(perro)
    }

    suspend fun eliminarTodos() {
        perroDao.eliminarTodos()
    }
}
