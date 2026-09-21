package com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth

import com.example.firebaseroomyarquitecturamvvm.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.register(email, password)
    }
}