package com.example.aplicacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.aplicacion.ui.theme.AplicacionTheme
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var useDarkTheme by rememberSaveable { mutableStateOf(false) }
            var cartItems by rememberSaveable { mutableStateOf(listOf<CartItem>()) }
            AplicacionTheme(useDarkTheme = useDarkTheme) {
                MainScreen(
                    useDarkTheme = useDarkTheme,
                    onThemeChange = { useDarkTheme = it },
                    cartItems = cartItems,
                    onAddToCart = { product ->
                        cartItems = cartItems + CartItem(product.id, product.name, product.price, product.imageUrl)
                    },
                    onRemoveFromCart = { cartItem ->
                        cartItems = cartItems.filter { it.id != cartItem.id }
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    useDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    cartItems: List<CartItem>,
    onAddToCart: (Product) -> Unit,
    onRemoveFromCart: (CartItem) -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("product_list")
            }, onRegisterClick = {
                navController.navigate("register")
            })
        }
        composable("product_list") {
            ProductListScreen(
                onAddToCart = onAddToCart,
                onBackClick = {
                    navController.popBackStack()
                },
                cartItemCount = cartItems.size,
                onCartClick = {
                    navController.navigate("shopping_cart")
                }
            )
        }
        composable("shopping_cart") {
            ShoppingCartScreen(
                cartItems = cartItems,
                onBackClick = {
                    navController.popBackStack()
                },
                onRemoveFromCart = onRemoveFromCart
            )
        }
        composable("register") {
            RegisterScreen(onRegisterSuccess = {
                navController.navigate("login")
            })
        }
    }
}