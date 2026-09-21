package com.example.firebaseroomyarquitecturamvvm.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object TaskList : Screen("task_list")
    data object TaskForm : Screen("task_form?taskId={taskId}&draftId={draftId}") {
        fun createRoute(taskId: String? = null, draftId: Int? = null) = "task_form?taskId=$taskId&draftId=$draftId"
    }
    data object Drafts : Screen("drafts")
}