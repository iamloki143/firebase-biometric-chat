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
class AppLockViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {
    private val _authEnabled = MutableStateFlow<Boolean?>(null)
    val authEnabled: StateFlow<Boolean?> = _authEnabled.asStateFlow()
    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    init {
        loadAuthEnabled()
    }

    private fun loadAuthEnabled() {
        viewModelScope.launch {
            _authEnabled.value = repository.isAuthEnabled()
        }
    }
    fun onUnlocked() {
        _isUnlocked.value = true
    }
    fun onLogout() {
        _isUnlocked.value = false
    }
    fun shouldShowLock(isLoggedIn: Boolean): Boolean {
        val auth = _authEnabled.value ?: return false
        return auth && isLoggedIn && !_isUnlocked.value
    }
}