// ShoppingCartScreen.kt
package com.example.aplicacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

data class CartItem(val id: Int, val name: String, val price: Double, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(cartItems: List<CartItem>, onBackClick: () -> Unit, onRemoveFromCart: (CartItem) -> Unit, onThemeChange: (Boolean) -> Unit, useDarkTheme: Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { onThemeChange(!useDarkTheme) }) {
                        Icon(
                            imageVector = Icons.Filled.Face,
                            contentDescription = "Cambiar Tema"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemRow(item = item, onRemove = { onRemoveFromCart(item) })
                }
            }
            CheckoutSection(total = cartItems.sumOf { it.price })
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = item.imageUrl),
            contentDescription = item.name,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.bodyLarge)
            Text("Precio: \$${item.price}", style = MaterialTheme.typography.bodyMedium)
        }

        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar"
            )
        }
    }
}

@Composable
fun CheckoutSection(total: Double) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Total: \$${"%.2f".format(total)}", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Acción de pagar */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Pagar")
        }
    }
}