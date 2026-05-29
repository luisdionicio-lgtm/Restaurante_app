package com.tecsup.restaurante_app.screens

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcurrencyTestScreen(navController: NavController) {
    var statusText by remember { mutableStateOf("Selecciona una prueba") }
    var isLoading by remember { mutableStateOf(false) }
    var userClicks by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    // Animación constante para demostrar que la UI vive
    val infiniteTransition = rememberInfiniteTransition(label = "rotate")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pruebas de Rendimiento", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // INDICADOR DE VIDA DE LA UI
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp).rotate(rotation),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Monitor de Respuesta UI", fontWeight = FontWeight.Bold)
                        Text(
                            if (isLoading) "Procesando en segundo plano..." else "UI Libre y Fluida",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isLoading) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN PARA QUE EL USUARIO INTERACTÚE
            Button(
                onClick = { userClicks++ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.TouchApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Clics del usuario mientras esperas: $userClicks")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pantalla de estado de la operación
            Surface(
                modifier = Modifier.fillMaxWidth().height(80.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 1. SIN HILO (Bloqueante)
            TestButton(
                title = "Sin Hilo (BLOQUEO)",
                description = "Congela TODO. El icono dejará de girar y no podrás hacer clic.",
                icon = Icons.Default.HourglassBottom,
                color = Color(0xFFC0392B),
                onClick = {
                    statusText = "¡UI CONGELADA! Intenta hacer clic arriba..."
                    // Bloqueamos el Main Thread
                    Thread.sleep(5000) 
                    statusText = "UI Liberada después de 5s"
                }
            )

            // 2. CON HILO (Background)
            TestButton(
                title = "Con Hilo (Separado)",
                description = "El icono sigue girando y puedes hacer clic arriba.",
                icon = Icons.Default.PrecisionManufacturing,
                color = Color(0xFF2980B9),
                onClick = {
                    statusText = "Hilo trabajando... ¡UI sigue viva!"
                    isLoading = true
                    Thread {
                        Thread.sleep(3000)
                        Handler(Looper.getMainLooper()).post {
                            statusText = "¡Hilo terminó con éxito!"
                            isLoading = false
                        }
                    }.start()
                }
            )

            // 3. SIN CORRUTINA (Legacy/Handler)
            TestButton(
                title = "Sin Corrutina (Legacy)",
                description = "Usa Handlers (forma antigua). No bloquea, pero es menos eficiente.",
                icon = Icons.Default.Timer,
                color = Color(0xFF7F8C8D),
                onClick = {
                    statusText = "Usando Handler antiguo..."
                    isLoading = true
                    // La forma "clásica" antes de corrutinas para tareas asíncronas
                    Handler(Looper.getMainLooper()).postDelayed({
                        statusText = "¡Terminado con Handler!"
                        isLoading = false
                    }, 3000)
                }
            )

            // 4. CON CORRUTINA (Moderno)
            TestButton(
                title = "Con Corrutina (Eficiente)",
                description = "Igual que el hilo, pero más ligero y moderno.",
                icon = Icons.Default.Bolt,
                color = Color(0xFF27AE60),
                onClick = {
                    scope.launch {
                        statusText = "Corrutina suspendida... UI fluida."
                        isLoading = true
                        delay(3000)
                        statusText = "¡Corrutina terminó con éxito!"
                        isLoading = false
                    }
                }
            )
        }
    }
}

@Composable
fun TestButton(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(50.dp)
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.padding(12.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
