package com.example.firebaseroomyarquitecturamvvm.ui.screen.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseroomyarquitecturamvvm.domain.model.Task
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth.GetCurrentUserUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.task.DeleteTaskUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.task.GetTasksUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskListUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        val userId = getCurrentUserUseCase() ?: return
        _uiState.update { it.copy(isLoading = true) }
        getTasksUseCase(userId)
            .onEach { tasks ->
                _uiState.update { it.copy(isLoading = false, tasks = tasks, errorMessage = null) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
            .launchIn(viewModelScope)
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task.copy(completed = !task.completed, updatedAt = System.currentTimeMillis()))
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
        }
    }
}