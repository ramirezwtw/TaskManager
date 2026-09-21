package com.example.firebaseroomyarquitecturamvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.firebaseroomyarquitecturamvvm.ui.navigation.NavGraph
import com.example.firebaseroomyarquitecturamvvm.ui.navigation.Screen
import com.example.firebaseroomyarquitecturamvvm.ui.screen.auth.AuthViewModel
import com.example.firebaseroomyarquitecturamvvm.ui.theme.FirebaseRoomYArquitecturaMVVMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseRoomYArquitecturaMVVMTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val startDestination = if (authViewModel.getCurrentUserId() != null) {
                        Screen.TaskList.route
                    } else {
                        Screen.Login.route
                    }
                    
                    NavGraph(
                        navController = navController,
                        startDestination = startDestination,
                        onLogout = { authViewModel.logout() }
                    )
                }
            }
        }
    }
}