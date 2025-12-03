package com.example.huellapp.DAO

import androidx.room.*
import com.example.huellapp.model.Perro
import kotlinx.coroutines.flow.Flow

@Dao
interface PerroDao {

    @Query("SELECT * FROM perro ORDER BY id DESC")
    fun obtenerPerros(): Flow<List<Perro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPerro(perro: Perro)

    @Delete
    suspend fun eliminarPerro(perro: Perro)

    @Query("DELETE FROM perro")
    suspend fun eliminarTodos()
}
