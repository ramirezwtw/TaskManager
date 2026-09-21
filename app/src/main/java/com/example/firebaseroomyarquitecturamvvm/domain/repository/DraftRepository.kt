package com.example.firebaseroomyarquitecturamvvm.domain.repository

import com.example.firebaseroomyarquitecturamvvm.domain.model.TaskDraft
import kotlinx.coroutines.flow.Flow

interface DraftRepository {
    fun observeDrafts(userId: String): Flow<List<TaskDraft>>
    suspend fun saveDraft(draft: TaskDraft): Result<Unit>
    suspend fun updateDraft(draft: TaskDraft): Result<Unit>
    suspend fun deleteDraft(draftId: Int): Result<Unit>
}