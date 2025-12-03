import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.huellapp.database.AppDatabase
import com.example.huellapp.repository.PerroRepository
import com.example.huellapp.viewmodel.PerroViewModel
import com.example.huellapp.viewmodel.PerroViewModelFactory
import com.example.huellapp.model.Perro







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPerrosScreen() {
    val context = LocalContext.current

    // Inicializar Repository y ViewModel
    val repository = remember {
        val dao = AppDatabase.getDatabase(context).perroDao()
        PerroRepository(dao)
    }

    val factory = remember { PerroViewModelFactory(repository) }
    val viewModel: PerroViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)

    val perros by viewModel.perros.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Perros") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar perro")
            }
        }
    ) { padding ->
        if (perros.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Pets,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No tienes perros registrados")
                    Text("Toca + para agregar uno", style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(perros) { perro ->
                    PerroCard(
                        perro = perro,
                        onDelete = { viewModel.eliminarPerro(perro) }
                    )
                }
            }
        }

        if (showAddDialog) {
            AgregarPerroDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { nuevoPerro ->
                    viewModel.agregarPerro(nuevoPerro)
                    showAddDialog = false
                }
            )
        }
    }
}

// ---------------------------------------
// Diálogo para agregar perro
// ---------------------------------------
@Composable
fun AgregarPerroDialog(
    onDismiss: () -> Unit,
    onConfirm: (Perro) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var temperamento by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> fotoUri = uri }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = "Agregar Perro",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Selector de foto
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(fotoUri),
                            contentDescription = "Foto del perro",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.CameraAlt,
                                contentDescription = "Agregar foto",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Agregar foto",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Campos de datos
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre *") },
                    leadingIcon = { Icon(Icons.Filled.Pets, null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = raza,
                    onValueChange = { raza = it },
                    label = { Text("Raza *") },
                    leadingIcon = { Icon(Icons.Filled.Category, null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = edad,
                    onValueChange = { if (it.all { c -> c.isDigit() }) edad = it },
                    label = { Text("Edad (años) *") },
                    leadingIcon = { Icon(Icons.Filled.CalendarMonth, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )


                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = peso,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) peso = it },
                    label = { Text("Peso (kg) *") },
                    leadingIcon = { Icon(Icons.Filled.MonitorWeight, null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true
                )


                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = temperamento,
                    onValueChange = { temperamento = it },
                    label = { Text("Temperamento") },
                    leadingIcon = { Icon(Icons.Filled.Mood, null) },
                    placeholder = { Text("Ej: Juguetón, tranquilo, activo...") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) { Text("Cancelar") }

                    Button(
                        onClick = {
                            if (nombre.isNotBlank() && raza.isNotBlank() && edad.isNotBlank() && peso.isNotBlank()) {
                                val nuevoPerro = Perro(
                                    nombre = nombre.trim(),
                                    raza = raza.trim(),
                                    edad = edad.toInt(),
                                    peso = peso.toFloat(),
                                    temperamento = temperamento.trim().ifBlank { "Sin especificar" },
                                    fotoUri = fotoUri?.toString()
                                )
                                onConfirm(nuevoPerro)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = nombre.isNotBlank() && raza.isNotBlank() && edad.isNotBlank() && peso.isNotBlank()
                    ) { Text("Guardar") }
                }
            }
        }
    }
}

// ---------------------------------------
// Card para mostrar perro
// ---------------------------------------
@Composable
fun PerroCard(perro: Perro, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                if (perro.fotoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(Uri.parse(perro.fotoUri)),
                        contentDescription = "Foto de ${perro.nombre}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.Pets,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(perro.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(perro.raza, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text("${perro.edad} años") }, modifier = Modifier.height(24.dp))
                    AssistChip(onClick = {}, label = { Text("${perro.peso} kg") }, modifier = Modifier.height(24.dp))
                }
            }

            Column {
                IconButton(onClick = { /* Detalles */ }) { Icon(Icons.Filled.KeyboardArrowRight, "Ver detalles") }
                IconButton(onClick = { showDeleteDialog = true }) { Icon(Icons.Filled.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error) }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Filled.Warning, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("¿Eliminar a ${perro.nombre}?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = { onDelete(); showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }
}
