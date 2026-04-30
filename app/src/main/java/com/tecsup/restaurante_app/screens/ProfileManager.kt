package com.tecsup.restaurante_app.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class UserData(
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val profileImageUrl: String = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=1000&auto=format&fit=crop"
)

object ProfileManager {
    var userData by mutableStateOf(
        UserData(
            name = "Luis Dionicio",
            email = "luis@gmail.com",
            phone = "987 654 321",
            address = "Av. Perú 123, Lima"
        )
    )

    fun updateProfile(name: String, email: String, phone: String, address: String) {
        userData = UserData(name, email, phone, address)
    }
}
