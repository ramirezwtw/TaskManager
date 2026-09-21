package com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth

import com.example.firebaseroomyarquitecturamvvm.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String? {
        return authRepository.getCurrentUserId()
    }
}