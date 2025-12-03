// Archivo: build.gradle.kts (Raíz del proyecto)

plugins {
    // Agrega la versión del plugin de Hilt aquí
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    // Agrega la versión del plugin de KSP aquí si no la tienes en libs.versions.toml
    id("com.google.devtools.ksp") version "2.0.21-1.0.25" apply false

    // Agrega aquí los plugins estándar de Android/Kotlin si no están en settings.gradle.kts
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}