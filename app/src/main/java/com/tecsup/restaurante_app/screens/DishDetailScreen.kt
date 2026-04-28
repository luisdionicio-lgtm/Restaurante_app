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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailScreen(dishId: Int, navController: NavController) {
    // Reutilizamos la data de ejemplo por ahora
    val dishes = listOf(
        Dish(1, "Papa a la Huancaína", "Papas cocidas con crema de ají amarillo y queso.", 15.0, "https://images.unsplash.com/photo-1626074353765-517a681e40be?q=80&w=200&auto=format&fit=crop", "Entradas"),
        Dish(2, "Ceviche Clásico", "Pescado fresco marinado en limón y especias.", 25.0, "https://images.unsplash.com/photo-1534766329978-f0847164ba0d?q=80&w=200&auto=format&fit=crop", "Entradas"),
        Dish(3, "Lomo Saltado", "Trozos de carne salteados con cebolla y tomate.", 35.0, "https://images.unsplash.com/photo-1626074353765-517a681e40be?q=80&w=200&auto=format&fit=crop", "Platos de Fondo"),
        Dish(4, "Ají de Gallina", "Crema espesa con base de ají amarillo y pollo.", 28.0, "https://images.unsplash.com/photo-1626074353765-517a681e40be?q=80&w=200&auto=format&fit=crop", "Platos de Fondo"),
        Dish(5, "Suspiro a la Limeña", "Dulce tradicional a base de manjar blanco.", 12.0, "https://images.unsplash.com/photo-1551024506-0bccd828d307?q=80&w=200&auto=format&fit=crop", "Postres"),
        Dish(6, "Picarones", "Anillos de masa frita con miel de chancaca.", 10.0, "https://images.unsplash.com/photo-1551024506-0bccd828d307?q=80&w=200&auto=format&fit=crop", "Postres"),
        Dish(7, "Chicha Morada", "Bebida de maíz morado, piña y especias.", 8.0, "https://images.unsplash.com/photo-1544145945-f904253d0c7b?q=80&w=200&auto=format&fit=crop", "Bebidas"),
        Dish(8, "Inca Kola", "La gaseosa del Perú.", 5.0, "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=200&auto=format&fit=crop", "Bebidas")
    )

    val dish = dishes.find { it.id == dishId } ?: return

    var quantity by remember { mutableStateOf(1) }

    Scaffold(
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

            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = dish.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "S/ ${"%.2f".format(dish.price)}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = dish.description + " Preparado con ingredientes frescos de la mejor calidad, siguiendo la receta tradicional que nos caracteriza.",
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
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Reducir")
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* Acción para agregar al pedido */ },
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
