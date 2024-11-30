package com.example.aplicacion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacion.viewmodel.ProfileViewModel
import com.google.ar.sceneform.Sun

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBackClick: () -> Unit, onThemeChange: (Boolean) -> Unit, useDarkTheme: Boolean, profileViewModel: ProfileViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    var tempUsername by remember { mutableStateOf(profileViewModel.username.value) }
    var tempEmail by remember { mutableStateOf(profileViewModel.email.value) }

    Scaffold(

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://example.com/profile.jpg"),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(profileViewModel.username.value, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(profileViewModel.email.value, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar Perfil")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Perfil")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Editar Perfil") },
            text = {
                Column {
                    OutlinedTextField(
                        value = tempUsername,
                        onValueChange = { tempUsername = it },
                        label = { Text("Nombre de Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Correo Electrónico") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    profileViewModel.updateUsername(tempUsername)
                    profileViewModel.updateEmail(tempEmail)
                    showDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}