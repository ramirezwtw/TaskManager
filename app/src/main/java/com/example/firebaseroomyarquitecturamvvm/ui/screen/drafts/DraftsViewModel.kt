package com.example.firebaseroomyarquitecturamvvm.ui.screen.drafts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseroomyarquitecturamvvm.domain.model.TaskDraft
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.auth.GetCurrentUserUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft.DeleteDraftUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft.GetDraftsUseCase
import com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft.PublishDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DraftsUiState(
    val isLoading: Boolean = false,
    val drafts: List<TaskDraft> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class DraftsViewModel @Inject constructor(
    private val getDraftsUseCase: GetDraftsUseCase,
    private val publishDraftUseCase: PublishDraftUseCase,
    private val deleteDraftUseCase: DeleteDraftUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DraftsUiState())
    val uiState: StateFlow<DraftsUiState> = _uiState.asStateFlow()

    init {
        loadDrafts()
    }

    private fun loadDrafts() {
        val userId = getCurrentUserUseCase() ?: return
        _uiState.update { it.copy(isLoading = true) }
        getDraftsUseCase(userId)
            .onEach { drafts ->
                _uiState.update { it.copy(isLoading = false, drafts = drafts, errorMessage = null) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
            .launchIn(viewModelScope)
    }

    fun publishDraft(draft: TaskDraft) {
        viewModelScope.launch {
            val result = publishDraftUseCase(draft)
            if (result.isFailure) {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message ?: "Failed to publish") }
            }
        }
    }

    fun deleteDraft(draftId: Int) {
        viewModelScope.launch {
            deleteDraftUseCase(draftId)
        }
    }
}