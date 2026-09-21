package com.example.firebaseroomyarquitecturamvvm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.firebaseroomyarquitecturamvvm.ui.screen.auth.LoginScreen
import com.example.firebaseroomyarquitecturamvvm.ui.screen.auth.RegisterScreen
import com.example.firebaseroomyarquitecturamvvm.ui.screen.drafts.DraftsScreen
import com.example.firebaseroomyarquitecturamvvm.ui.screen.taskform.TaskFormScreen
import com.example.firebaseroomyarquitecturamvvm.ui.screen.tasklist.TaskListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { 
                    navController.navigate(Screen.TaskList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = { 
                    navController.navigate(Screen.TaskList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.TaskList.route) {
            TaskListScreen(
                onNavigateToForm = { taskId -> navController.navigate(Screen.TaskForm.createRoute(taskId)) },
                onNavigateToDrafts = { navController.navigate(Screen.Drafts.route) },
                onLogout = { 
                    onLogout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.TaskList.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.TaskForm.route,
            arguments = listOf(
                navArgument("taskId") { 
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("draftId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            TaskFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Drafts.route) {
            DraftsScreen(
                onNavigateBack = { navController.popBackStack() },
                onEditDraft = { draftId -> navController.navigate(Screen.TaskForm.createRoute(draftId = draftId)) }
            )
        }
    }
}