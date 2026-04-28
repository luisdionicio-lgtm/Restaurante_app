package com.tecsup.restaurante_app.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Menu : Screen("menu")
    object Order : Screen("order")
    object Profile : Screen("profile")
}