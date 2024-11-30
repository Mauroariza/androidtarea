package com.example.aplicacion.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import com.example.aplicacion.navigation.Screen

@Composable
fun LoggedOutBottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Login,
        Screen.Register
    )
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun LoggedInBottomNavigationBar(navController: NavHostController, onLogout: () -> Unit) {
    val items = listOf(
        Screen.Profile,
        Screen.Settings,
        Screen.ShoppingCart
    )
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ExitToApp, contentDescription = "Logout") },
            label = { Text("Logout") },
            selected = false,
            onClick = onLogout
        )
    }
}