package com.example.huellapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.huellapp.DAO.PaseoDao
import com.example.huellapp.DAO.PerroDao
import com.example.huellapp.DAO.UserDao
import com.example.huellapp.model.Paseo
import com.example.huellapp.model.Perro
import com.example.huellapp.model.Usuario

@Database(
    entities = [Perro::class, Paseo::class, Usuario::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun perroDao(): PerroDao
    abstract fun paseoDao(): PaseoDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huellapp_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

