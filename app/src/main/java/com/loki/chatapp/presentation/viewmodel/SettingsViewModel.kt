package com.loki.chatapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.chatapp.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    var authEnabled by mutableStateOf<Boolean?>(null)
        private set

    suspend fun loadSettings() {
        authEnabled = repository.isAuthEnabled()
    }

    fun onToggleChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAuthEnable(enabled)
            authEnabled = enabled
        }
    }
}