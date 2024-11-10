package com.example.aplicacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val title: String) {
    object Profile : Screen("profile", Icons.Filled.Person, "Profile")
    object Settings : Screen("settings", Icons.Filled.Settings, "Settings")
    object ShoppingCart : Screen("shopping_cart", Icons.Filled.ShoppingCart, "Cart")
    object Login : Screen("login", Icons.Filled.Person, "Login")
    object Register : Screen("register", Icons.Filled.Person, "Register")
}