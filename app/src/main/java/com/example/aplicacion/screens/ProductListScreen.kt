package com.example.aplicacion.screens

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
    onCartClick: () -> Unit,
    onThemeChange: (Boolean) -> Unit,
    useDarkTheme: Boolean
) {
    val products = remember {
        listOf(
            Product(1, "Manzana", 19.99, "https://acortar.link/x2do5E"),
            Product(2, "Fresa", 29.99, "https://acortar.link/jnABfG"),
            Product(3, "Pera", 9.99, "https://acortar.link/CrYvaV")
        )
    }

    Scaffold(
        topBar = {

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
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(product.imageUrl),
            contentDescription = product.name,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = product.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "$${product.price}", style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = { onAddToCart(product) }) {
            Icon(Icons.Filled.ShoppingCart, contentDescription = "Add to cart")
        }
    }
}