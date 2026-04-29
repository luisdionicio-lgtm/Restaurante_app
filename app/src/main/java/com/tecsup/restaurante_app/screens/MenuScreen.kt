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
            Dish(1, "Papa a la Huancaína", "Papas cocidas con crema de ají amarillo y queso.", 15.0, "https://imgmedia.buenazo.pe/1200x660/buenazo/original/2020/09/25/5f6eaf8e2810e95b5c5da50c.jpg", "Entradas"),
            Dish(2, "Ceviche Clásico", "Pescado fresco marinado en limón y especias.", 25.0, "https://i0.wp.com/www.cetprocajamarca.edu.pe/wp-content/uploads/2024/12/cebiche.jpg?resize=735%2C413&ssl=1", "Entradas"),
            Dish(3, "Lomo Saltado", "Trozos de carne salteados con cebolla y tomate.", 35.0, "https://static.wixstatic.com/media/9755d8_b2d98eade0814b17a67fdf7d95888fdc~mv2.png/v1/fill/w_1000,h_563,al_c,q_90,usm_0.66_1.00_0.01/9755d8_b2d98eade0814b17a67fdf7d95888fdc~mv2.png", "Platos de Fondo"),
            Dish(4, "Ají de Gallina", "Crema espesa con base de ají amarillo y pollo.", 28.0, "https://mirecetadehoy.com/assets/images/2025/11/aji-de-gallina_800x534.webp", "Platos de Fondo"),
            Dish(5, "Suspiro a la Limeña", "Dulce tradicional a base de manjar blanco.", 10.0, "https://www.nestleprofessional-latam.com/sites/default/files/styles/np_recipe_detail/public/2023-09/SUSPIRO-LIMEN%E2%95%A0%C3%A2O.jpg?itok=arjP6tu6", "Postres"),
            Dish(6, "Picarones", "Anillos de masa frita con miel de chancaca.", 10.0, "https://imagescdn.estarbien.com.pe/blt1dce4402dc518e3a/68b28124f8270074d884d734/PORTADA_Picarones_Saludables.jpg?format=auto&quality=85", "Postres"),
            Dish(7, "Chicha Morada", "Bebida de maíz morado, piña y especias.", 8.0, "https://i.ytimg.com/vi/gnJeTv-FuLU/maxresdefault.jpg", "Bebidas"),
            Dish(8, "Inca Kola", "La gaseosa del Perú.", 5.0, "https://jamacarabanchel.com/wp-content/uploads/2020/04/INKACOLA-300.png", "Bebidas")
        )
    }

    val filteredDishes = if (selectedCategory == "Todos") dishes else dishes.filter { it.category == selectedCategory }

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
                    .padding(top = 12.dp, bottom = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.height(40.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = dish.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "S/ ${"%.2f".format(dish.price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
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