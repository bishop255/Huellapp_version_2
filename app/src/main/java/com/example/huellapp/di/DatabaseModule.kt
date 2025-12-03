package com.example.huellapp.di

import android.content.Context
import androidx.room.Room
import com.example.huellapp.DAO.PaseoDao // Importación necesaria
import com.example.huellapp.DAO.PerroDao // Importación necesaria
import com.example.huellapp.DAO.UserDao
import com.example.huellapp.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // 1. Provee la Base de Datos (Singleton)
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "huellapp_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    // 2. Provee el UserDao
    @Provides
    fun provideUserDao(
        db: AppDatabase
    ): UserDao = db.userDao()

    // 3. Provee el PaseoDao (¡SOLUCIONA EL ERROR ORIGINAL!)
    @Provides
    fun providePaseoDao(
        db: AppDatabase
    ): PaseoDao = db.paseoDao()

    // 4. Provee el PerroDao (Asumiendo que también se usará en un Repository)
    @Provides
    fun providePerroDao(
        db: AppDatabase
    ): PerroDao = db.perroDao()
}