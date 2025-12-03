package com.example.huellapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.huellapp.model.Paseador
import com.example.huellapp.model.Paseo
import com.example.huellapp.viewmodel.PaseoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaseadoresScreen(
    listaPerros: List<String>,
    paseoViewModel: PaseoViewModel
) {

    // Paseo activo y historial del ViewModel
    val paseoActivo = paseoViewModel.paseoActivo
    val historial = paseoViewModel.historial

    val paseadores = remember {
        listOf(
            Paseador("1", "Carlos Rodríguez", 4.8f, 3, 15000, true),
            Paseador("2", "Danae Guerrero", 4.9f, 5, 18000, true),
            Paseador("3", "Juan Pérez", 4.5f, 2, 12000, false),
            Paseador("4", "Jhotzean Oquendo", 4.2f, 4, 16000, true),
            Paseador("5", "Ambar Soto", 4.0f, 1, 12000, false)
        )
    }

    //---------------------------------------
    // ESTADOS LOCALES
    //---------------------------------------
    var mostrarSeleccionPerro by remember { mutableStateOf(false) }
    var perroSeleccionado by remember { mutableStateOf<String?>(null) }
    var paseadorSeleccionado by remember { mutableStateOf<Paseador?>(null) }

    //---------------------------------------
    // TIMER DEL PASEO
    //---------------------------------------
    if (paseoActivo != null && !paseoActivo.finalizado) {
        LaunchedEffect(paseoActivo.codigo) {
            while (!paseoActivo.finalizado) {
                delay(1000)

                paseoViewModel.actualizarTiempo()

                if (paseoViewModel.paseoActivo?.tiempoTranscurrido ?: 0 >= 60) {
                    paseoViewModel.finalizarPaseo()
                }
            }
        }
    }

    //---------------------------------------
    // UI PRINCIPAL
    //---------------------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paseadores Disponibles") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {

            // Lista de paseadores
            items(paseadores) { paseador ->
                PaseadorCard(
                    paseador = paseador,
                    paseoEnCurso = paseoActivo?.paseador == paseador && !paseoActivo.finalizado,
                    paseoFinalizado = paseoActivo?.paseador == paseador && paseoActivo.finalizado,
                    onSolicitar = {
                        paseadorSeleccionado = paseador
                        perroSeleccionado = null
                        mostrarSeleccionPerro = true
                    }
                )
            }

            // Historial
            item {
                Spacer(Modifier.height(20.dp))
                Text("Historial de Paseos", style = MaterialTheme.typography.titleMedium)

                historial.forEach { paseo ->
                    Text("• ${paseo.codigo} — ${paseo.perro} — ${paseo.tiempoTranscurrido}s")
                }
            }
        }
    }

    //---------------------------------------
    // DIÁLOGO DE SELECCIÓN DE PERRO
    //---------------------------------------
    if (mostrarSeleccionPerro) {

        AlertDialog(
            onDismissRequest = { mostrarSeleccionPerro = false },
            title = { Text("Selecciona un Perro") },
            text = {
                Column {
                    listaPerros.forEach { perro ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = perro == perroSeleccionado,
                                onClick = { perroSeleccionado = perro }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(perro)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (perroSeleccionado != null && paseadorSeleccionado != null) {
                            paseoViewModel.iniciarPaseo(
                                paseadorSeleccionado!!,
                                perroSeleccionado!!
                            )
                            mostrarSeleccionPerro = false
                        }
                    }
                ) { Text("Iniciar Paseo") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeleccionPerro = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

//----------------------------------------------------
// CARD DEL PASEADOR
//----------------------------------------------------
@Composable
fun PaseadorCard(
    paseador: Paseador,
    paseoEnCurso: Boolean,
    paseoFinalizado: Boolean,
    onSolicitar: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Surface(
                    modifier = Modifier.size(56.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Person, contentDescription = null)
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {

                    Text(paseador.nombre, fontWeight = FontWeight.Bold)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("${paseador.calificacion} • ${paseador.experiencia} años exp.")
                    }

                    Text(
                        "$${paseador.tarifa}/paseo",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onSolicitar,
                modifier = Modifier.fillMaxWidth(),
                enabled = paseador.disponible && !paseoEnCurso
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Solicitar Paseo")
            }

            if (paseoEnCurso) {
                Text("Paseo en curso...", color = MaterialTheme.colorScheme.secondary)
            }

            if (paseoFinalizado) {
                Text("¡Paseo finalizado!", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
