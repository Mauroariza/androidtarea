package com.example.aplicacion

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class ProfileViewModel : ViewModel() {
    private val _username = mutableStateOf("Nombre de Usuario")
    val username: State<String> = _username

    private val _email = mutableStateOf("usuario@example.com")
    val email: State<String> = _email

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBackClick: () -> Unit, onThemeChange: (Boolean) -> Unit, useDarkTheme: Boolean, profileViewModel: ProfileViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    var tempUsername by remember { mutableStateOf(profileViewModel.username.value) }
    var tempEmail by remember { mutableStateOf(profileViewModel.email.value) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
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