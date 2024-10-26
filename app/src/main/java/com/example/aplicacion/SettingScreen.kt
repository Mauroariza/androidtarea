package com.example.aplicacion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackClick: () -> Unit, onThemeChange: (Boolean) -> Unit, useDarkTheme: Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuraciones") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Configuraciones Generales", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            SettingsItem(icon = Icons.Filled.Notifications, title = "Notificaciones", description = "Administrar notificaciones")
            SettingsItem(icon = Icons.Filled.Info, title = "Privacidad", description = "Configuraciones de privacidad")
            // Agrega más elementos de configuración según sea necesario
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text(description, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp))
        }
    }
}