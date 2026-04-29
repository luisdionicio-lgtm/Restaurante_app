package com.tecsup.restaurante_app.screens

import androidx.compose.runtime.mutableStateListOf

data class CartItem(
    val dish: Dish,
    var quantity: Int
)

object CartManager {
    val items = mutableStateListOf<CartItem>()

    fun addToCart(dish: Dish, quantity: Int) {
        val existingItem = items.find { it.dish.id == dish.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            items.add(CartItem(dish, quantity))
        }
    }

    fun clearCart() {
        items.clear()
    }

    fun getTotal(): Double {
        return items.sumOf { it.dish.price * it.quantity }
    }
}