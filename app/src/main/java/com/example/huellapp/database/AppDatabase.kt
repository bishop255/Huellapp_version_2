package com.example.huellapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.huellapp.DAO.PaseoDao
import com.example.huellapp.DAO.PerroDao
import com.example.huellapp.model.Paseo
import com.example.huellapp.model.Perro

@Database(
    entities = [Perro::class, Paseo::class], // ✅ TODAS LAS ENTIDADES AQUÍ
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun perroDao(): PerroDao
    abstract fun paseoDao(): PaseoDao

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
                    .fallbackToDestructiveMigration() // útil durante desarrollo
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

