package com.loki.chatapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.chatapp.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _authEnabled = MutableStateFlow(false)
    val authEnabled: StateFlow<Boolean> = _authEnabled

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language

    init {
        loadAll()
    }

    private fun loadAll() {
        viewModelScope.launch {
            _authEnabled.value = repository.isAuthEnabled()
            _isDarkTheme.value = repository.isDarkTheme()
            _language.value = repository.getLanguage()
        }
    }

    fun loadSettings(){}
    fun loadTheme(){}
    fun loadLanguage(){}

    fun onToggleChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAuthEnable(enabled)
            _authEnabled.value = enabled
        }
    }

    fun onThemeChanged(enabled: Boolean) {
        viewModelScope.launch {
            repository.setDarkTheme(enabled)
            _isDarkTheme.value = enabled
        }
    }

    fun onLanguageChanged(lang: String) {
        viewModelScope.launch {
            repository.setLanguage(lang)
            _language.value = lang
        }
    }
}
