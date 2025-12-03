package com.example.huellapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huellapp.ui.theme.HuellappTheme
import com.example.huellapp.view.*
import com.example.huellapp.viewmodel.UserViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HuellappTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("welcome") {
            WelcomeScreen(navController)
        }

        composable("login") {
            val vm: UserViewModel = hiltViewModel()
            LoginScreen(navController, vm)
        }

        composable("register") {
            val vm: UserViewModel = hiltViewModel()
            RegisterScreen(navController, vm)
        }

        composable("home") {
            MainScreenDueno(navController)
        }

        composable("perfil") {
            val vm: UserViewModel = hiltViewModel() // Agrega esta línea
            PerfilScreen(navController, vm) // Pasa el viewModel
        }

        composable("qr") {
            QrScannerScreen(
                onQrScanned = { valor ->
                    println("QR leído: $valor")
                    navController.navigate("home")
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }
    }
}