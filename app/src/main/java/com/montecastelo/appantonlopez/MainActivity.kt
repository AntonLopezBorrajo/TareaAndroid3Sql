package com.montecastelo.appantonlopez

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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

class MainActivity : ComponentActivity() {

    private val databaseHelper by lazy { ExerciseDatabaseHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (databaseHelper.getAllExercises().isEmpty()) {
            databaseHelper.addExercise("Flexiones")
            databaseHelper.addExercise("Peso muerto")
        }

        setContent {
            MainScreen(databaseHelper)
        }
    }
}

// Base de datos SQLite
class ExerciseDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "exercises.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_EXERCISES = "exercises"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_EXERCISES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISES")
        onCreate(db)
    }

    fun addExercise(name: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
        }
        db.insert(TABLE_EXERCISES, null, values)
        db.close()
    }

    fun getAllExercises(): List<String> {
        val exercises = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.query(TABLE_EXERCISES, arrayOf(COLUMN_NAME), null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                exercises.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return exercises
    }

    fun updateExercise(oldName: String, newName: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, newName)
        }
        db.update(TABLE_EXERCISES, values, "$COLUMN_NAME = ?", arrayOf(oldName))
        db.close()
    }

    fun deleteExercise(name: String) {
        val db = writableDatabase
        db.delete(TABLE_EXERCISES, "$COLUMN_NAME = ?", arrayOf(name))
        db.close()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(databaseHelper: ExerciseDatabaseHelper) {
    val exercises = remember { mutableStateListOf<String>().apply { addAll(databaseHelper.getAllExercises()) } }
    var showDialog by remember { mutableStateOf(false) }
    var currentExercise by remember { mutableStateOf("") }
    var newExerciseName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Automonitor") }
            )
        },
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
                    items(exercises) { item ->
                        ListItem(
                            text = item,
                            onDelete = {
                                databaseHelper.deleteExercise(item)
                                exercises.remove(item)
                            },
                            onEdit = {
                                currentExercise = item
                                newExerciseName = item
                                showDialog = true
                            }
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                val newExercise = "Nuevo"
                                databaseHelper.addExercise(newExercise)
                                exercises.add(newExercise)
                            },
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
                            databaseHelper.updateExercise(currentExercise, newExerciseName)
                            val index = exercises.indexOf(currentExercise)
                            if (index != -1) {
                                exercises[index] = newExerciseName
                            }
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
            Text(
                text = text,
                fontSize = 14.sp
            )
            Row {
                Button(onClick = onEdit) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete) {
                    Text("Eliminar")
                }
            }
        }
    }
}






