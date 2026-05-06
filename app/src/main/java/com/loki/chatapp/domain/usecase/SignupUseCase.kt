package com.loki.chatapp.domain.usecase

import com.loki.chatapp.domain.repository.AuthRepository

class SignupUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, password: String)=
        repo.signup(email,password)
}