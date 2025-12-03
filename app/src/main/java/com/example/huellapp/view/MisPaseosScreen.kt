package com.example.huellapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.huellapp.model.EstadoPaseo
import com.example.huellapp.model.Paseo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPaseosScreen() {
    val paseos = remember {
        listOf(
            Paseo(
                id = 1,
                codigo = "P001",
                paseadorId = 1,
                perroId = 1,
                duracionSegundos = 3600, // 60 minutos
                fecha = System.currentTimeMillis(),
                hora = "10:00",
                estado = EstadoPaseo.PROGRAMADO.name
            ),
            Paseo(
                id = 2,
                codigo = "P002",
                paseadorId = 2,
                perroId = 2,
                duracionSegundos = 2700, // 45 minutos
                fecha = System.currentTimeMillis() - 86400000, // Ayer
                hora = "16:30",
                estado = EstadoPaseo.COMPLETADO.name
            ),
            Paseo(
                id = 3,
                codigo = "P003",
                paseadorId = 3,
                perroId = 3,
                duracionSegundos = 1800, // 30 minutos
                fecha = System.currentTimeMillis() - 172800000, // Hace 2 días
                hora = "09:00",
                estado = EstadoPaseo.COMPLETADO.name
            ),
            Paseo(
                id = 4,
                codigo = "P004",
                paseadorId = 4,
                perroId = 1,
                duracionSegundos = 3600, // 60 minutos
                fecha = System.currentTimeMillis() - 259200000, // Hace 3 días
                hora = "15:00",
                estado = EstadoPaseo.COMPLETADO.name
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Paseos") },
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(paseos) { paseo ->
                PaseoCard(paseo)
            }
        }
    }
}

@Composable
fun PaseoCard(paseo: Paseo) {
    val estadoPaseo = EstadoPaseo.valueOf(paseo.estado)

    val estadoColor = when (estadoPaseo) {
        EstadoPaseo.PROGRAMADO -> MaterialTheme.colorScheme.primary
        EstadoPaseo.EN_CURSO -> MaterialTheme.colorScheme.tertiary
        EstadoPaseo.COMPLETADO -> MaterialTheme.colorScheme.secondary
        EstadoPaseo.CANCELADO -> MaterialTheme.colorScheme.error
    }

    // Convertir fecha timestamp a formato legible
    val dateFormat = SimpleDateFormat("dd MMM", Locale("es", "ES"))
    val fechaFormateada = dateFormat.format(Date(paseo.fecha))

    // Convertir duración de segundos a minutos
    val duracionMinutos = paseo.duracionSegundos / 60

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Perro ID: ${paseo.perroId}", // Aquí deberías obtener el nombre del perro
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    color = estadoColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = when (estadoPaseo) {
                            EstadoPaseo.PROGRAMADO -> "Programado"
                            EstadoPaseo.EN_CURSO -> "En Curso"
                            EstadoPaseo.COMPLETADO -> "Completado"
                            EstadoPaseo.CANCELADO -> "Cancelado"
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = estadoColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.DirectionsWalk,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Paseador ID: ${paseo.paseadorId}", // Aquí deberías obtener el nombre
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$fechaFormateada • ${paseo.hora}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$duracionMinutos min",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (estadoPaseo == EstadoPaseo.PROGRAMADO) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Cancelar */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = { /* Ver detalles */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Ver Detalles")
                    }
                }
            }
        }
    }
}