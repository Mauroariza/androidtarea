package com.example.aplicacion.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

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
    var isAppBarVisible by remember { mutableStateOf(true) }

    // Definimos el ancho y alto de la AppBar para usarlo en posicionamiento
    val appBarHeight = 56.dp // Altura estándar de AppBar en Material Design

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Flecha en la mitad superior de la AppBar (siempre visible)
        IconButton(
            onClick = { isAppBarVisible = !isAppBarVisible },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = if (isAppBarVisible) 8.dp else 16.dp)
                .zIndex(2f) // Asegura que esté encima de la AppBar
        ) {
            Icon(
                imageVector = if (isAppBarVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isAppBarVisible) "Ocultar barra superior" else "Mostrar barra superior"
            )
        }

        // Barra superior con visibilidad animada
        AnimatedVisibility(
            visible = isAppBarVisible,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 300)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f) // Para que esté debajo de la flecha
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
                            imageVector = if (useDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
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
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito"
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}