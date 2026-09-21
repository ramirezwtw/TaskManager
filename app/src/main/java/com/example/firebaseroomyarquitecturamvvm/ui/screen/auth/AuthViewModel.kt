package com.example.firebaseroomyarquitecturamvvm.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth.GetCurrentUserUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth.LoginUserUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth.LogoutUserUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth.RegisterUserUseCase
import com.example.firebaseroomyarquitecturamvvm.ui.state.OperationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<OperationState>(OperationState.Idle)
    val authState: StateFlow<OperationState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = OperationState.Loading
            val result = loginUserUseCase(email, password)
            _authState.value = if (result.isSuccess) OperationState.Success else OperationState.Error(result.exceptionOrNull()?.message ?: "Login failed")
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = OperationState.Loading
            val result = registerUserUseCase(email, password)
            _authState.value = if (result.isSuccess) OperationState.Success else OperationState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUserUseCase()
        }
    }

    fun getCurrentUserId() = getCurrentUserUseCase()

    fun resetState() {
        _authState.value = OperationState.Idle
    }
}