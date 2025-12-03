package com.example.huellapp.repository

import com.example.huellapp.DAO.PerroDao
import com.example.huellapp.model.Perro
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerroRepository @Inject constructor(private val perroDao: PerroDao) {

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