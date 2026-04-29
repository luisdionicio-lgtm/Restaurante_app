package com.tecsup.restaurante_app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailScreen(dishId: Int, navController: NavController) {
    val dishes = listOf(
        Dish(1, "Papa a la Huancaína", "Papas cocidas con crema de ají amarillo y queso.", 15.0, "https://imgmedia.buenazo.pe/1200x660/buenazo/original/2020/09/25/5f6eaf8e2810e95b5c5da50c.jpg", "Entradas"),
        Dish(2, "Ceviche Clásico", "Pescado fresco marinado en limón y especias.", 25.0, "https://i0.wp.com/www.cetprocajamarca.edu.pe/wp-content/uploads/2024/12/cebiche.jpg?resize=735%2C413&ssl=1", "Entradas"),
        Dish(3, "Lomo Saltado", "Trozos de carne salteados con cebolla y tomate.", 35.0, "https://static.wixstatic.com/media/9755d8_b2d98eade0814b17a67fdf7d95888fdc~mv2.png/v1/fill/w_1000,h_563,al_c,q_90,usm_0.66_1.00_0.01/9755d8_b2d98eade0814b17a67fdf7d95888fdc~mv2.png", "Platos de Fondo"),
        Dish(4, "Ají de Gallina", "Crema espesa con base de ají amarillo y pollo.", 28.0, "https://mirecetadehoy.com/assets/images/2025/11/aji-de-gallina_800x534.webp", "Platos de Fondo"),
        Dish(5, "Suspiro a la Limeña", "Dulce tradicional a base de manjar blanco.", 10.0, "https://www.nestleprofessional-latam.com/sites/default/files/styles/np_recipe_detail/public/2023-09/SUSPIRO-LIMEN%E2%95%A0%C3%A2O.jpg?itok=arjP6tu6", "Postres"),
        Dish(6, "Picarones", "Anillos de masa frita con miel de chancaca.", 10.0, "https://imagescdn.estarbien.com.pe/blt1dce4402dc518e3a/68b28124f8270074d884d734/PORTADA_Picarones_Saludables.jpg?format=auto&quality=85", "Postres"),
        Dish(7, "Chicha Morada", "Bebida de maíz morado, piña y especias.", 8.0, "https://i.ytimg.com/vi/gnJeTv-FuLU/maxresdefault.jpg", "Bebidas"),
        Dish(8, "Inca Kola", "La gaseosa del Perú.", 5.0, "https://jamacarabanchel.com/wp-content/uploads/2020/04/INKACOLA-300.png", "Bebidas")
    )

    val dish = dishes.find { it.id == dishId } ?: return
    var quantity by remember { mutableStateOf(1) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(dish.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = dish.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(dish.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                Text(
                    text = "S/ ${"%.2f".format(dish.price)}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                Text(
                    text = dish.description + " Preparado con ingredientes frescos de la mejor calidad.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
                        Icon(Icons.Default.Remove, contentDescription = "Reducir")
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { quantity++ }) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        CartManager.addToCart(dish, quantity)
                        scope.launch {
                            snackbarHostState.showSnackbar("${dish.name} agregado al pedido")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Agregar al pedido (S/ ${"%.2f".format(dish.price * quantity)})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DishDetailScreenPreview() {
    DishDetailScreen(dishId = 1, navController = rememberNavController())
}