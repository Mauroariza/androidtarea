package com.example.aplicacion

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen() {
    val context = LocalContext.current
    val locationService = remember { LocationService(context) }
    val coroutineScope = rememberCoroutineScope()
    var location by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            locationService.getLocationUpdates().collect {
                location = it
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Real-Time Geolocation") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            location?.let {
                Text("Latitude: ${it.latitude}")
                Text("Longitude: ${it.longitude}")
            } ?: run {
                Text("Getting location...")
            }
        }
    }
}