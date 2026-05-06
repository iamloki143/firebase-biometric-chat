package com.loki.chatapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.chatapp.domain.usecase.LoginUseCase
import com.loki.chatapp.domain.usecase.SignupUseCase
import com.loki.chatapp.presentation.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase
): ViewModel() {
    var state by mutableStateOf<AuthState>(AuthState.Idle)
        private set
    fun login(email:String, password: String){
        viewModelScope.launch {
            state= AuthState.Loading
            val result =loginUseCase(email,password)
            state=result.fold(
                onSuccess = { AuthState.Success},
                onFailure = { AuthState.Error(it.message ?: "Error")}
            )
        }
    }
    fun signup(email: String,password: String){
        viewModelScope.launch {
            state= AuthState.Loading
            val result=signupUseCase(email,password)
            state=result.fold(
                onSuccess = {
                    AuthState.Success
                },
                onFailure = { AuthState.Error(it.message ?: "Error")}
            )
        }
    }
}