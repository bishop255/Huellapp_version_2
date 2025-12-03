package com.example.huellapp.repository

import com.example.huellapp.DAO.UserDao
import com.example.huellapp.model.Usuario
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun login(email: String, password: String): Usuario? {
        return userDao.login(email, password)
    }

    suspend fun register(usuario: Usuario): Boolean {
        return try {
            val existingUser = userDao.getUserByEmail(usuario.email)
            if (existingUser != null) {
                false
            } else {
                userDao.insert(usuario)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserByEmail(email: String): Usuario? {
        return userDao.getUserByEmail(email)
    }
}
