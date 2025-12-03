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
import com.example.huellapp.model.EstadoPaseo
import com.example.huellapp.model.Paseador
import com.example.huellapp.model.Paseo
import com.example.huellapp.viewmodel.PaseoViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaseadoresScreen(
    listaPerros: List<String>,
    paseoViewModel: PaseoViewModel
) {
    // Observar estados del ViewModel
    val paseosActivos by paseoViewModel.paseosActivos.collectAsState(initial = emptyList())
    val historial by paseoViewModel.historial.collectAsState(initial = emptyList())

    val paseadores = remember {
        listOf(
            Paseador(id = 1, nombre = "Carlos Rodríguez", calificacion = 4.8f, experiencia = 3, tarifa = 15000, disponible = true),
            Paseador(id = 2, nombre = "Danae Guerrero", calificacion = 4.9f, experiencia = 5, tarifa = 18000, disponible = true),
            Paseador(id = 3, nombre = "Juan Pérez", calificacion = 4.5f, experiencia = 2, tarifa = 12000, disponible = false),
            Paseador(id = 4, nombre = "Jhotzean Oquendo", calificacion = 4.2f, experiencia = 4, tarifa = 16000, disponible = true),
            Paseador(id = 5, nombre = "Ambar Soto", calificacion = 4.0f, experiencia = 1, tarifa = 12000, disponible = false)
        )
    }

    // Estados locales
    var mostrarSeleccionPerro by remember { mutableStateOf(false) }
    var perroSeleccionado by remember { mutableStateOf<String?>(null) }
    var paseadorSeleccionado by remember { mutableStateOf<Paseador?>(null) }

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
            // Paseos activos
            if (paseosActivos.isNotEmpty()) {
                item {
                    Text(
                        "Paseos en Curso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(paseosActivos) { paseo ->
                    PaseoEnCursoCard(paseo, paseoViewModel)
                }

                item { Spacer(Modifier.height(16.dp)) }
            }

            // Lista de paseadores
            item {
                Text(
                    "Paseadores Disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(paseadores) { paseador ->
                val tienePaseoActivo = paseosActivos.any { it.paseadorId == paseador.id }

                PaseadorCard(
                    paseador = paseador,
                    paseoEnCurso = tienePaseoActivo,
                    onSolicitar = {
                        paseadorSeleccionado = paseador
                        perroSeleccionado = null
                        mostrarSeleccionPerro = true
                    }
                )
            }

            // Historial
            if (historial.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(20.dp))
                    Text(
                        "Historial de Paseos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(historial) { paseo ->
                    HistorialPaseoItem(paseo)
                }
            }
        }
    }

    // Diálogo de selección de perro
    if (mostrarSeleccionPerro) {
        AlertDialog(
            onDismissRequest = { mostrarSeleccionPerro = false },
            title = { Text("Selecciona un Perro") },
            text = {
                Column {
                    listaPerros.forEachIndexed { index, perro ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
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
                            val perroIndex = listaPerros.indexOf(perroSeleccionado!!) + 1
                            paseoViewModel.iniciarPaseo(
                                paseadorId = paseadorSeleccionado!!.id,
                                perroId = perroIndex,
                                perroNombre = perroSeleccionado!!
                            )
                            mostrarSeleccionPerro = false
                        }
                    },
                    enabled = perroSeleccionado != null
                ) {
                    Text("Iniciar Paseo")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeleccionPerro = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun PaseadorCard(
    paseador: Paseador,
    paseoEnCurso: Boolean,
    onSolicitar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
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
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "${paseador.calificacion} • ${paseador.experiencia} años exp.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Text(
                        "$${paseador.tarifa}/paseo",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
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
                Text(if (paseoEnCurso) "Paseo en Curso" else "Solicitar Paseo")
            }
        }
    }
}

@Composable
fun PaseoEnCursoCard(paseo: Paseo, viewModel: PaseoViewModel) {
    var tiempoTranscurrido by remember { mutableStateOf(0) }

    LaunchedEffect(paseo.id) {
        while (paseo.estado == EstadoPaseo.EN_CURSO.name) {
            delay(1000)
            tiempoTranscurrido++

            // Auto finalizar después de la duración programada
            if (tiempoTranscurrido >= paseo.duracionSegundos) {
                viewModel.finalizarPaseo(paseo.id)
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Paseo en Curso",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text("Código: ${paseo.codigo}")
            Text("Perro ID: ${paseo.perroId}")
            Text("Tiempo: ${tiempoTranscurrido / 60}:${String.format("%02d", tiempoTranscurrido % 60)}")

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { viewModel.finalizarPaseo(paseo.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar Paseo")
            }
        }
    }
}

@Composable
fun HistorialPaseoItem(paseo: Paseo) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))
    val fechaFormateada = dateFormat.format(Date(paseo.fecha))
    val duracionMinutos = paseo.duracionSegundos / 60

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Código: ${paseo.codigo}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Perro ID: ${paseo.perroId}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "$fechaFormateada • ${paseo.hora}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "$duracionMinutos min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}