package com.example.firebaseroomyarquitecturamvvm.ui.screen.taskform

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseroomyarquitecturamvvm.domain.model.Task
import com.example.firebaseroomyarquitecturamvvm.domain.model.TaskDraft
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth.GetCurrentUserUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft.SaveDraftUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.task.CreateTaskUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.task.GetTasksUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.task.UpdateTaskUseCase
import com.example.firebaseroomyarquitecturamvvm.ui.state.OperationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft.DeleteDraftUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft.GetDraftsUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft.UpdateDraftUseCase

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val saveDraftUseCase: SaveDraftUseCase,
    private val updateDraftUseCase: UpdateDraftUseCase,
    private val deleteDraftUseCase: DeleteDraftUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val getDraftsUseCase: GetDraftsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: String? = savedStateHandle["taskId"]
    private val draftId: Int = savedStateHandle["draftId"] ?: -1
    
    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState: StateFlow<OperationState> = _operationState.asStateFlow()

    private val _task = MutableStateFlow(Task())
    val task: StateFlow<Task> = _task.asStateFlow()

    init {
        val userId = getCurrentUserUseCase()
        if (userId != null) {
            if (taskId != null) {
                viewModelScope.launch {
                    getTasksUseCase(userId).take(1).collect { tasks ->
                        tasks.find { it.id == taskId }?.let { _task.value = it }
                    }
                }
            } else if (draftId != -1) {
                viewModelScope.launch {
                    getDraftsUseCase(userId).take(1).collect { drafts ->
                        drafts.find { it.id == draftId }?.let { draft ->
                            _task.value = Task(title = draft.title, description = draft.description)
                        }
                    }
                }
            }
        }
    }

    fun onTitleChange(title: String) { _task.update { it.copy(title = title) } }
    fun onDescriptionChange(desc: String) { _task.update { it.copy(description = desc) } }

    fun saveTask() {
        val userId = getCurrentUserUseCase() ?: return
        viewModelScope.launch {
            _operationState.value = OperationState.Loading
            val currentTask = _task.value.copy(
                ownerId = userId,
                createdAt = if (_task.value.createdAt == 0L) System.currentTimeMillis() else _task.value.createdAt,
                updatedAt = System.currentTimeMillis()
            )
            val result = if (taskId == null) {
                createTaskUseCase(currentTask).map { Unit }
            } else {
                updateTaskUseCase(currentTask)
            }
            if (result.isSuccess && draftId != -1) {
                deleteDraftUseCase(draftId)
            }
            _operationState.value = if (result.isSuccess) OperationState.Success else OperationState.Error(result.exceptionOrNull()?.message ?: "Error saving task")
        }
    }

    fun saveAsDraft() {
        val userId = getCurrentUserUseCase() ?: return
        viewModelScope.launch {
            _operationState.value = OperationState.Loading
            val draft = TaskDraft(
                id = if (draftId != -1) draftId else 0,
                ownerId = userId,
                title = _task.value.title,
                description = _task.value.description,
                savedAt = System.currentTimeMillis()
            )
            val result = if (draftId == -1) saveDraftUseCase(draft) else updateDraftUseCase(draft)
            _operationState.value = if (result.isSuccess) OperationState.Success else OperationState.Error(result.exceptionOrNull()?.message ?: "Error saving draft")
        }
    }
}