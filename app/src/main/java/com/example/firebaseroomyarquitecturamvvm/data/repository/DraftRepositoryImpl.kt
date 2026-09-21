package com.example.firebaseroomyarquitecturamvvm.data.repository

import com.example.firebaseroomyarquitecturamvvm.data.local.dao.TaskDraftDao
import com.example.firebaseroomyarquitecturamvvm.data.mapper.toDomain
import com.example.firebaseroomyarquitecturamvvm.data.mapper.toEntity
import com.example.firebaseroomyarquitecturamvvm.domain.model.TaskDraft
import com.example.firebaseroomyarquitecturamvvm.domain.repository.DraftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DraftRepositoryImpl @Inject constructor(
    private val taskDraftDao: TaskDraftDao
) : DraftRepository {
    override fun observeDrafts(userId: String): Flow<List<TaskDraft>> {
        return taskDraftDao.observeDrafts(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveDraft(draft: TaskDraft): Result<Unit> {
        return try {
            taskDraftDao.insertDraft(draft.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDraft(draft: TaskDraft): Result<Unit> {
        return try {
            taskDraftDao.updateDraft(draft.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteDraft(draftId: Int): Result<Unit> {
        return try {
            taskDraftDao.deleteDraft(draftId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}