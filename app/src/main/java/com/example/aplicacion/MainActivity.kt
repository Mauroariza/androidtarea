package com.example.aplicacion

import com.example.aplicacion.ProfileViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import com.example.aplicacion.ui.theme.AplicacionTheme
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.*
import androidx.compose.ui.Modifier

import androidx.activity.viewModels

class MainActivity : ComponentActivity() {
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var useDarkTheme by rememberSaveable { mutableStateOf(false) }
            var cartItems by rememberSaveable { mutableStateOf(listOf<CartItem>()) }
            AplicacionTheme(useDarkTheme = useDarkTheme) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController = navController) }
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = "login", modifier = Modifier.padding(paddingValues)) {
                        composable("login") {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate("product_list")
                            }, onRegisterClick = {
                                navController.navigate("register")
                            }, onThemeChange = { useDarkTheme = it }, useDarkTheme = useDarkTheme)
                        }
                        composable("product_list") {
                            ProductListScreen(
                                onAddToCart = { product ->
                                    cartItems = cartItems + CartItem(product.id, product.name, product.price, product.imageUrl)
                                },
                                onBackClick = { navController.popBackStack() },
                                cartItemCount = cartItems.size,
                                onCartClick = { navController.navigate("shopping_cart") },
                                onThemeChange = { useDarkTheme = it },
                                useDarkTheme = useDarkTheme
                            )
                        }
                        composable("shopping_cart") {
                            ShoppingCartScreen(
                                cartItems = cartItems,
                                onBackClick = { navController.popBackStack() },
                                onRemoveFromCart = { cartItem ->
                                    cartItems = cartItems.filter { it.id != cartItem.id }
                                },
                                onThemeChange = { useDarkTheme = it },
                                useDarkTheme = useDarkTheme
                            )
                        }
                        composable("register") {
                            RegisterScreen(onRegisterSuccess = {
                                navController.navigate("login")
                            }, onThemeChange = { useDarkTheme = it }, useDarkTheme = useDarkTheme)
                        }
                        composable("profile") {
                            ProfileScreen(onBackClick = { navController.popBackStack() }, onThemeChange = { useDarkTheme = it }, useDarkTheme = useDarkTheme, profileViewModel = profileViewModel)
                        }
                        composable("settings") {
                            SettingsScreen(onBackClick = { navController.popBackStack() }, onThemeChange = { useDarkTheme = it }, useDarkTheme = useDarkTheme)
                        }
                    }
                }
            }
        }
    }
}