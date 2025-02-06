package com.montecastelo.appantonlopez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.montecastelo.appantonlopez.data.Ejercicios
import com.montecastelo.appantonlopez.data.EjerciciosDB
import com.montecastelo.appantonlopez.viewmodel.EjerciciosViewModel
import com.montecastelo.appantonlopez.viewmodel.EjerciciosViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var database: EjerciciosDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la base de datos Room
        database = Room.databaseBuilder(
            applicationContext,
            EjerciciosDB::class.java,
            "EjerciciosBasesDatos"
        ).build()

        // Crear el ViewModel usando el Factory
        val viewModelFactory = EjerciciosViewModelFactory(database.EjerciciosBase())
        val ejerciciosViewModel = ViewModelProvider(this, viewModelFactory).get(EjerciciosViewModel::class.java)

        setContent {
            MainScreen(ejerciciosViewModel)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: EjerciciosViewModel) {
    val ejercicios by viewModel.ejercicios.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var currentExercise by remember { mutableStateOf("") }
    var newExerciseName by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Automonitor") }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ejercicios) { item ->
                        ListItem(
                            text = item.nombre,
                            onDelete = { viewModel.eliminarEjercicio(item.nombre) },
                            onEdit = {
                                currentExercise = item.nombre
                                newExerciseName = item.nombre
                                showDialog = true
                            }
                        )
                    }
                    item {
                        Button(
                            onClick = { viewModel.agregarEjercicio("Nuevo") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Agregar ejercicio")
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Editar Ejercicio") },
                    text = {
                        Column {
                            Text("Nombre actual: $currentExercise")
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = newExerciseName,
                                onValueChange = { newExerciseName = it },
                                label = { Text("Nuevo nombre") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.actualizarEjercicio(currentExercise, newExerciseName)
                            showDialog = false
                        }) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    )
}


@Composable
fun ListItem(text: String, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text, fontSize = 14.sp)
            Row {
                Button(onClick = onEdit) { Text("Editar") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete) { Text("Eliminar") }
            }
        }
    }
}
