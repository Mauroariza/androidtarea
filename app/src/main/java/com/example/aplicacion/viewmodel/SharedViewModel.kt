package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class SharedViewModel : ViewModel() {
    private val _currentTitle = mutableStateOf("App Title")
    val currentTitle: State<String> = _currentTitle

    fun updateTitle(newTitle: String) {
        _currentTitle.value = newTitle
    }
}