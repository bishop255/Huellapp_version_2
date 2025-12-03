package com.example.huellapp.view

import MisPerrosScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.huellapp.viewmodel.PerroViewModel
import com.example.huellapp.viewmodel.PerroViewModelFactory
import com.example.huellapp.viewmodel.PaseoViewModel
import com.example.huellapp.viewmodel.UserViewModel
import com.example.huellapp.repository.PerroRepository
import com.example.huellapp.database.AppDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenDueno(navController: NavHostController) {
    val bottomNavController = rememberNavController()

    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { PerroRepository(database.perroDao()) }
    val factory = remember { PerroViewModelFactory(repository) }

    val perroViewModel: PerroViewModel = viewModel(factory = factory)
    val userViewModel: UserViewModel = hiltViewModel()
    val paseoViewModel: PaseoViewModel = hiltViewModel()

    val listaPerros by perroViewModel.perros.collectAsState()
    val listaNombresPerros = listaPerros.map { it.nombre }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HuellApp") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("qr")
                    }) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Escanear QR"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(bottomNavController)
        }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "mis_perros",
            modifier = Modifier.padding(padding)
        ) {
            composable("mis_perros") {
                MisPerrosScreen()
            }

            composable("paseadores") {
                PaseadoresScreen(
                    listaPerros = listaNombresPerros,
                    paseoViewModel = paseoViewModel
                )
            }

            composable("mis_paseos") {
                MisPaseosScreen()
            }

            composable("perfil") {
                PerfilScreen(
                    navController = navController,
                    userViewModel = userViewModel
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Mis Perros", Icons.Filled.Pets, "mis_perros"),
        BottomNavItem("Paseadores", Icons.Filled.DirectionsWalk, "paseadores"),
        BottomNavItem("Mis Paseos", Icons.Filled.CalendarMonth, "mis_paseos"),
        BottomNavItem("Perfil", Icons.Filled.Person, "perfil")
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)