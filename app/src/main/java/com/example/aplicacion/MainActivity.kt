package com.example.aplicacion

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.aplicacion.components.CommonTopAppBar
import com.example.aplicacion.components.LoggedInBottomNavigationBar
import com.example.aplicacion.components.LoggedOutBottomNavigationBar
import com.example.aplicacion.models.CartItem
import com.example.aplicacion.screens.*
import com.example.aplicacion.ui.theme.AplicacionTheme
import com.example.aplicacion.viewmodel.ProfileViewModel
import com.example.aplicacion.viewmodel.SharedViewModel
import com.google.android.gms.maps.model.LatLng

class MainActivity : ComponentActivity() {
    private val profileViewModel: ProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var useDarkTheme: Boolean by rememberSaveable { mutableStateOf(false) }
            var cartItems: List<CartItem> by rememberSaveable { mutableStateOf(listOf()) }
            var isLoggedIn: Boolean by rememberSaveable { mutableStateOf(false) }

            AplicacionTheme(useDarkTheme = useDarkTheme) {
                val navController: NavHostController = rememberNavController()
                Scaffold(
                    topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        val currentRoute = currentDestination?.route

                        val showCartIcon = currentRoute == "product_list" || currentRoute == "shopping_cart"
                        CommonTopAppBar(
                            title = sharedViewModel.currentTitle.value,
                            useDarkTheme = useDarkTheme,
                            onThemeChange = { useDarkTheme = it },
                            onBackClick = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                }
                            },
                            cartItemCount = if (showCartIcon) cartItems.size else null,
                            onCartClick = if (showCartIcon) {
                                { navController.navigate("shopping_cart") }
                            } else null
                        )
                    },
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
                    },
                    content = { paddingValues: PaddingValues ->
                        NavHost(navController = navController, startDestination = "login", modifier = Modifier.padding(paddingValues)) {
                            composable("login") {
                                sharedViewModel.updateTitle("Iniciar Sesión")
                                LoginScreen(
                                    onLoginSuccess = {
                                        isLoggedIn = true
                                        navController.navigate("product_list") {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        }
                                    },
                                    onRegisterClick = {
                                        navController.navigate("register")
                                    },
                                    onThemeChange = { useDarkTheme = it },
                                    useDarkTheme = useDarkTheme
                                )
                            }
                            composable("product_list") {
                                sharedViewModel.updateTitle("Lista de Productos")
                                ProductListScreen(
                                    onAddToCart = { product: Product ->
                                        val existingItem: CartItem? = cartItems.find { it.id == product.id }
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
                                sharedViewModel.updateTitle("Carrito de Compras")
                                ShoppingCartScreen(
                                    cartItems = cartItems,
                                    onBackClick = { navController.popBackStack() },
                                    onRemoveFromCart = { cartItem: CartItem ->
                                        cartItems = cartItems.filter { it.id != cartItem.id }
                                    },
                                    onUpdateQuantity = { cartItem: CartItem, newQuantity: Int ->
                                        cartItems = cartItems.map {
                                            if (it.id == cartItem.id) it.copy(quantity = newQuantity) else it
                                        }
                                    },
                                    onThemeChange = { useDarkTheme = it },
                                    useDarkTheme = useDarkTheme
                                )
                            }
                            composable("register") {
                                sharedViewModel.updateTitle("Registro")
                                RegisterScreen(onRegisterSuccess = {
                                    isLoggedIn = true
                                    navController.navigate("product_list") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    }
                                }, onThemeChange = { useDarkTheme = it }, useDarkTheme = useDarkTheme)
                            }
                            composable("profile") {
                                ProfileScreen(onBackClick = { navController.popBackStack() }, onThemeChange = { useDarkTheme = it }, useDarkTheme = useDarkTheme, profileViewModel = profileViewModel)
                            }
                            composable("settings") {
                                sharedViewModel.updateTitle("Configuración")
                                SettingsScreen(
                                    onBackClick = { navController.popBackStack() },
                                    onThemeChange = { useDarkTheme = it },
                                    useDarkTheme = useDarkTheme,
                                    onMapClick = { location: LatLng ->
                                        navController.navigate("map/${location.latitude},${location.longitude}")
                                    }
                                )
                            }
                            composable("location") {
                                sharedViewModel.updateTitle("Ubicación")
                                LocationScreen()
                            }
                            composable("map/{initialLocationLat},{initialLocationLng}") { backStackEntry ->
                                val initialLocation: LatLng = backStackEntry.arguments?.let {
                                    val lat: Double = it.getString("initialLocationLat")?.toDoubleOrNull() ?: 0.0
                                    val lng: Double = it.getString("initialLocationLng")?.toDoubleOrNull() ?: 0.0
                                    LatLng(lat, lng)
                                } ?: LatLng(0.0, 0.0)
                                sharedViewModel.updateTitle("Mapa")
                                MapScreen(initialLocation = initialLocation)
                            }
                        }
                    }
                )
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