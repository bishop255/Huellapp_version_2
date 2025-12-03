package com.example.huellapp.DAO

import androidx.room.*
import com.example.huellapp.model.Paseo
import kotlinx.coroutines.flow.Flow

@Dao
interface PaseoDao {
    @Query("SELECT * FROM paseos ORDER BY fecha DESC, hora DESC")
    fun getAllPaseos(): Flow<List<Paseo>>

    @Query("SELECT * FROM paseos WHERE estado = :estado ORDER BY fecha DESC")
    fun getPaseosByEstado(estado: String): Flow<List<Paseo>>

    @Query("SELECT * FROM paseos WHERE perroId = :perroId ORDER BY fecha DESC")
    fun getPaseosByPerro(perroId: Int): Flow<List<Paseo>>

    @Query("SELECT * FROM paseos ORDER BY id DESC")
    fun obtenerHistorial(): Flow<List<Paseo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaseo(paseo: Paseo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaseos(paseos: List<Paseo>)

    @Update
    suspend fun updatePaseo(paseo: Paseo) // Agrega este método

    @Query("UPDATE paseos SET estado = :estado WHERE id = :paseoId") // Agrega este método
    suspend fun updateEstado(paseoId: Int, estado: String)

    @Query("DELETE FROM paseos")
    suspend fun deleteAllPaseos()
}