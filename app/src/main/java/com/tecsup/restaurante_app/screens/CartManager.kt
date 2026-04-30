package com.tecsup.restaurante_app.screens

import androidx.compose.runtime.mutableStateListOf

object CartManager {
    val items = mutableStateListOf<OrderItem>()

    fun addToCart(dish: Dish, quantity: Int) {
        val existingItem = items.find { it.dish.id == dish.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            items.add(OrderItem(dish, quantity))
        }
    }

    fun updateQuantity(dishId: Int, newQuantity: Int) {
        val index = items.indexOfFirst { it.dish.id == dishId }
        if (index != -1) {
            if (newQuantity > 0) {
                items[index] = items[index].copy(quantity = newQuantity)
            } else {
                removeFromCart(dishId)
            }
        }
    }

    fun removeFromCart(dishId: Int) {
        items.removeAll { it.dish.id == dishId }
    }

    fun clearCart() {
        items.clear()
    }

    fun getTotal(): Double {
        return items.sumOf { it.dish.price * it.quantity }
    }
}
