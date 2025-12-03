// Archivo: app/build.gradle.kts (Nivel de Módulo 'app')

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // 1. Plugins de Dagger Hilt y KSP/kapt
    id("com.google.dagger.hilt.android")
    // Usar KSP es más moderno y rápido que kapt
    id("com.google.devtools.ksp")

    // Si necesitas kapt para otras librerías que no soportan KSP
    // kotlin("kapt")
}

android {
    namespace = "com.example.huellapp"
    compileSdk = 36 // Mantener

    defaultConfig {
        applicationId = "com.example.huellapp"
        minSdk = 35 // Mantener
        targetSdk = 36 // Mantener
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Usa JavaVersion.VERSION_1_8 si es necesario, pero 17 está bien
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // La configuración de Kotlin es más limpia así
    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            // Limpieza de empaquetado, la mantienes
            excludes += "google/**"
            excludes += "META-INF/INDEX.LIST"
        }
    }
}

// Nota: La sección configurations.all con resolutionStrategy es inusual y a veces innecesaria.
// Si tu proyecto funciona sin ella, es mejor eliminarla. La mantengo por ahora.
configurations.all {
    resolutionStrategy {
        force("org.jetbrains:annotations:23.0.0")
    }
}

dependencies {

    // --- Compose y Kotlin Core ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime)
    implementation("androidx.compose.material:material-icons-extended:1.7.4")

    // --- Hilt (Dagger Hilt) ---
    // Usamos la versión estable 2.51.1 (o superior)
    val hilt_version = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    // Reemplaza 'kapt' con 'ksp' para el compilador si es posible
    ksp("com.google.dagger:hilt-compiler:$hilt_version")

    // Hilt Navigation (versión estable)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // Subida a 1.2.0

    // --- Coroutines y Lifecycle ---
    val coroutines_version = "1.8.1" // Actualizado a una versión más reciente
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    val lifecycle_version = "2.8.6" // Mantener
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")

    // --- Navigation ---
    implementation("androidx.navigation:navigation-compose:2.8.7") // Mantener

    // --- Network (Retrofit/OkHttp) ---
    val okhttp_version = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")

    val retrofit_version = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")

    // --- JSON Serialization ---
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3") // Mantener

    // --- Room (con KSP) ---
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version") // Usando ksp

    // --- Imagen (Coil) ---
    implementation("io.coil-kt:coil-compose:2.6.0") // Actualizado a 2.6.0

    // --- Location ---
    // Mantener 21.0.1
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // --- CameraX ---
    val camerax_version = "1.3.4"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    // --- ML Kit Barcode Scanning (incluye QR) ---
    implementation("com.google.mlkit:barcode-scanning:17.2.0") // Mantener

    // --- Tests ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}