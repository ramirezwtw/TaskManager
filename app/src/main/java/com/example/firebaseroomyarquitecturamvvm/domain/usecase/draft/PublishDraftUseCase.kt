package com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft

import com.example.firebaseroomyarquitecturamvvm.domain.model.Task
import com.example.firebaseroomyarquitecturamvvm.domain.model.TaskDraft
import com.example.firebaseroomyarquitecturamvvm.domain.repository.DraftRepository
import com.example.firebaseroomyarquitecturamvvm.domain.repository.TaskRepository
import javax.inject.Inject

class PublishDraftUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val draftRepository: DraftRepository
) {
    suspend operator fun invoke(draft: TaskDraft): Result<Unit> {
        val task = Task(
            ownerId = draft.ownerId,
            title = draft.title,
            description = draft.description,
            completed = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        val result = taskRepository.createTask(task)
        return if (result.isSuccess) {
            draftRepository.deleteDraft(draft.id)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Failed to publish draft"))
        }
    }
}