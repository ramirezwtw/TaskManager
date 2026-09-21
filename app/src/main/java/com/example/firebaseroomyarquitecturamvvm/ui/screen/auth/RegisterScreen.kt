package com.example.firebaseroomyarquitecturamvvm.ui.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firebaseroomyarquitecturamvvm.ui.state.OperationState

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is OperationState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6
    val passwordsMatch = password == confirmPassword

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email, 
            onValueChange = { email = it }, 
            label = { Text("Email") }, 
            modifier = Modifier.fillMaxWidth(),
            isError = email.isNotEmpty() && !isEmailValid
        )
        if (email.isNotEmpty() && !isEmailValid) {
            Text("Formato de correo inválido", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        
        OutlinedTextField(
            value = password, 
            onValueChange = { password = it }, 
            label = { Text("Password (mín. 6 caracteres)") }, 
            modifier = Modifier.fillMaxWidth(), 
            visualTransformation = PasswordVisualTransformation(),
            isError = password.isNotEmpty() && !isPasswordValid
        )
        
        OutlinedTextField(
            value = confirmPassword, 
            onValueChange = { confirmPassword = it }, 
            label = { Text("Confirm Password") }, 
            modifier = Modifier.fillMaxWidth(), 
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPassword.isNotEmpty() && !passwordsMatch
        )
        if (confirmPassword.isNotEmpty() && !passwordsMatch) {
            Text("Las contraseñas no coinciden", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (authState is OperationState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.register(email, password) }, 
                modifier = Modifier.fillMaxWidth(),
                enabled = isEmailValid && isPasswordValid && passwordsMatch
            ) {
                Text("Register")
            }
            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login")
            }
        }
        if (authState is OperationState.Error) {
            Text(text = (authState as OperationState.Error).message, color = MaterialTheme.colorScheme.error)
        }
    }
}