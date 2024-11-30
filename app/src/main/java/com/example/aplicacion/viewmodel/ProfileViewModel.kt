package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

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