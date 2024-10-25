package com.example.aplicacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

data class Product(val id: Int, val name: String, val price: Double, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onAddToCart: (Product) -> Unit,
    onBackClick: () -> Unit,
    cartItemCount: Int,
    onCartClick: () -> Unit
) {
    val products = remember {
        listOf(
            Product(1, "Producto 1", 19.99, "https://acortar.link/x2do5E"),
            Product(2, "Producto 2", 29.99, "https://acortar.link/x2do5E"),
            Product(3, "Producto 3", 9.99, "https://acortar.link/x2do5E")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de Productos") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        BadgedBox(badge = {
                            if (cartItemCount > 0) {
                                Badge { Text(cartItemCount.toString()) }
                            }
                        }) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(products) { product ->
                ProductRow(product = product, onAddToCart = onAddToCart)
            }
        }
    }
}

@Composable
fun ProductRow(product: Product, onAddToCart: (Product) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = product.imageUrl),
            contentDescription = product.name,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, style = MaterialTheme.typography.bodyLarge)
            Text("Precio: \$${product.price}", style = MaterialTheme.typography.bodyMedium)
        }

        Button(onClick = { onAddToCart(product) }) {
            Text("Agregar al Carrito")
        }
    }
}