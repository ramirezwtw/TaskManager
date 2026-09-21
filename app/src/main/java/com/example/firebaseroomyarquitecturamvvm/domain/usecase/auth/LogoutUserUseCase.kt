package com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth

import com.example.firebaseroomyarquitecturamvvm.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}