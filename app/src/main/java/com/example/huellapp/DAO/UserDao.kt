package com.example.huellapp.DAO

import androidx.room.*
import com.example.huellapp.model.Usuario

@Dao
interface UserDao {
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email")
    suspend fun getUserByEmail(email: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario)

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)

}