package com.montecastelo.appantonlopez

import android.os.Bundle
import android.util.Log
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.montecastelo.appantonlopez.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    // TAG para logs
    private val TAG = "MainActivityLifecycle"
    private val SCREEN_ROTATION_TAG = "ScreenRotationTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Aplicación abierta o actividad creada.")
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Actividad visible.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Actividad en primer plano.")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Actividad minimizada o ya no en primer plano.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Actividad ya no visible.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Actividad destruida (cerrada).")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Actividad restaurada después de detenerse.")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(TAG, "onLowMemory: Queda poca memoria en el dispositivo/emulador.")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(SCREEN_ROTATION_TAG, "Rotación: La pantalla ahora está en modo horizontal.")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(SCREEN_ROTATION_TAG, "Rotación: La pantalla ahora está en modo vertical.")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val items = listOf("Flexiones", "Peso muerto", "Sentadillas", "Bicicleta estática")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Automonitor")
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Texto descriptivo
                Text(
                    text = "Bienvenido. Aquí puedes ver tu lista de ejercicio.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {

                    val image = painterResource(id = R.drawable.gimnasio)
                    Image(
                        painter = image,
                        contentDescription = "Imagen del gimnasio",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                // Lista de elementos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        ListItem(text = item)
                    }
                }
            }
        }
    )
}

@Composable
fun ListItem(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp
        )
    }
}

