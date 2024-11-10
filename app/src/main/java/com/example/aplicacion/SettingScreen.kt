// SettingsScreen.kt
package com.example.aplicacion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onThemeChange: (Boolean) -> Unit,
    useDarkTheme: Boolean,
    onMapClick: (LatLng) -> Unit
) {
    val context = LocalContext.current
    val locationService = remember { LocationService(context) }
    val coroutineScope = rememberCoroutineScope()
    var location by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            locationService.getLocationUpdates().collect { locationData ->
                location = LatLng(locationData.latitude, locationData.longitude)
                location?.let { onMapClick(it) }
            }
        }
    }

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
            SettingsItem(icon = Icons.Filled.LocationOn, title = "Geolocalización", description = "Ver ubicación en tiempo real")
            SettingsItem(icon = Icons.Filled.LocationOn, title = "Mapa", description = "Ver mapa")
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, description: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
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