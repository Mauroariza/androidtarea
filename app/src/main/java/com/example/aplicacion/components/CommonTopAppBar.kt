package com.example.aplicacion.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    title: String,
    useDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onBackClick: (() -> Unit)? = null,
    cartItemCount: Int? = null,
    onCartClick: (() -> Unit)? = null
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text(title) },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            }
        },
        actions = {
            IconButton(onClick = { onThemeChange(!useDarkTheme) }) {
                Icon(
                    imageVector = if (useDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Cambiar Tema"
                )
            }
            if (onCartClick != null && cartItemCount != null) {
                IconButton(onClick = onCartClick) {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge { Text(cartItemCount.toString()) }
                            }
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                }
            }
        }
    )
}