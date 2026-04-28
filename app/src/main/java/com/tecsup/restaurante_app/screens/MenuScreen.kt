package com.tecsup.restaurante_app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

import com.tecsup.restaurante_app.navigation.Screen

data class Dish(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavController) {
    val categories = listOf("Todos", "Entradas", "Platos de Fondo", "Postres", "Bebidas")
    var selectedCategory by remember { mutableStateOf("Todos") }

    val dishes = remember {
        listOf(
            Dish(1, "Papa a la Huancaína", "Papas cocidas con crema de ají amarillo y queso.", 15.0, "https://images.unsplash.com/photo-1626074353765-517a681e40be?q=80&w=200&auto=format&fit=crop", "Entradas"),
            Dish(2, "Ceviche Clásico", "Pescado fresco marinado en limón y especias.", 25.0, "https://images.unsplash.com/photo-1534766329978-f0847164ba0d?q=80&w=200&auto=format&fit=crop", "Entradas"),
            Dish(3, "Lomo Saltado", "Trozos de carne salteados con cebolla y tomate.", 35.0, "https://images.unsplash.com/photo-1626074353765-517a681e40be?q=80&w=200&auto=format&fit=crop", "Platos de Fondo"),
            Dish(4, "Ají de Gallina", "Crema espesa con base de ají amarillo y pollo.", 28.0, "https://images.unsplash.com/photo-1626074353765-517a681e40be?q=80&w=200&auto=format&fit=crop", "Platos de Fondo"),
            Dish(5, "Suspiro a la Limeña", "Dulce tradicional a base de manjar blanco.", 12.0, "https://images.unsplash.com/photo-1551024506-0bccd828d307?q=80&w=200&auto=format&fit=crop", "Postres"),
            Dish(6, "Picarones", "Anillos de masa frita con miel de chancaca.", 10.0, "https://images.unsplash.com/photo-1551024506-0bccd828d307?q=80&w=200&auto=format&fit=crop", "Postres"),
            Dish(7, "Chicha Morada", "Bebida de maíz morado, piña y especias.", 8.0, "https://images.unsplash.com/photo-1544145945-f904253d0c7b?q=80&w=200&auto=format&fit=crop", "Bebidas"),
            Dish(8, "Inca Kola", "La gaseosa del Perú.", 5.0, "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=200&auto=format&fit=crop", "Bebidas")
        )
    }

    val filteredDishes = if (selectedCategory == "Todos") {
        dishes
    } else {
        dishes.filter { it.category == selectedCategory }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestro Menú") },
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
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredDishes) { dish ->
                    DishItem(dish = dish) {
                        navController.navigate(Screen.DishDetail.createRoute(dish.id))
                    }
                }
            }
        }
    }
}

@Composable
fun DishItem(dish: Dish, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = dish.imageUrl,
                contentDescription = dish.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dish.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dish.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Text(
                    text = "S/ ${"%.2f".format(dish.price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen(navController = rememberNavController())
}
