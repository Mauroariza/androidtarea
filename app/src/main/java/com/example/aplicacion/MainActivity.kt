package com.example.aplicacion

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.*
import com.example.aplicacion.ui.theme.AplicacionTheme
import com.google.android.gms.maps.model.LatLng

class MainActivity : ComponentActivity() {
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var useDarkTheme by rememberSaveable { mutableStateOf(false) }
            var cartItems by rememberSaveable { mutableStateOf(listOf<CartItem>()) }
            var isLoggedIn by rememberSaveable { mutableStateOf(false) }
            AplicacionTheme(useDarkTheme = useDarkTheme) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        if (isLoggedIn) {
                            LoggedInBottomNavigationBar(navController = navController, onLogout = {
                                isLoggedIn = false
                                navController.navigate("login") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            })
                        } else {
                            LoggedOutBottomNavigationBar(navController = navController)
                        }
                    }
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = "login", modifier = Modifier.padding(paddingValues)) {
                        composable("login") {
                            LoginScreen(onLoginSuccess = {
                                isLoggedIn = true
                                navController.navigate("product_list") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            }, onRegisterClick = {
                                navController.navigate("register")
                            }, onThemeChange = { useDarkTheme = it }, useDarkTheme = useDarkTheme)
                        }
                        composable("product_list") {
                            ProductListScreen(
                                onAddToCart = { product ->
                                    val existingItem = cartItems.find { it.id == product.id }
                                    if (existingItem != null) {
                                        cartItems = cartItems.map {
                                            if (it.id == product.id) it.copy(quantity = it.quantity + 1) else it
                                        }
                                    } else {
                                        cartItems = cartItems + CartItem(product.id, product.name, product.price, product.imageUrl, 1)
                                    }
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
                                onUpdateQuantity = { cartItem, newQuantity ->
                                    cartItems = cartItems.map {
                                        if (it.id == cartItem.id) it.copy(quantity = newQuantity) else it
                                    }
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
                            SettingsScreen(
                                onBackClick = { navController.popBackStack() },
                                onThemeChange = { useDarkTheme = it },
                                useDarkTheme = useDarkTheme,
                                onMapClick = { location ->
                                    navController.navigate("map/${location.latitude},${location.longitude}")
                                }
                            )
                        }
                        composable("location") {
                            LocationScreen()
                        }
                        composable("map/{initialLocationLat},{initialLocationLng}") { backStackEntry ->
                            val initialLocation = backStackEntry.arguments?.let {
                                val lat = it.getString("initialLocationLat")?.toDoubleOrNull() ?: 0.0
                                val lng = it.getString("initialLocationLng")?.toDoubleOrNull() ?: 0.0
                                LatLng(lat, lng)
                            } ?: LatLng(0.0, 0.0)
                            MapScreen(initialLocation = initialLocation)
                        }
                    }
                }
            }
        }
        requestLocationPermission()
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your app.
        } else {
            // Explain to the user that the feature is unavailable because the features requires a permission that the user has denied.
        }
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the location
            }
            else -> {
                // You can directly ask for the permission.
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}