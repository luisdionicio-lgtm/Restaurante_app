package com.tecsup.restaurante_app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.restaurante_app.screens.LoginScreen
import com.tecsup.restaurante_app.screens.HomeScreen
import com.tecsup.restaurante_app.screens.MenuScreen
import com.tecsup.restaurante_app.screens.DishDetailScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Menu.route) {
            MenuScreen(navController)
        }

        composable(
            route = Screen.DishDetail.route,
            arguments = listOf(navArgument("dishId") { type = NavType.IntType })
        ) { backStackEntry ->
            val dishId = backStackEntry.arguments?.getInt("dishId") ?: 0
            DishDetailScreen(dishId = dishId, navController = navController)
        }

        composable(Screen.Order.route) {
            PlaceholderScreen("Mi Pedido")
        }

        composable(Screen.Profile.route) {
            PlaceholderScreen("Mi Perfil")
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Pantalla de $name (En construcción)",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}