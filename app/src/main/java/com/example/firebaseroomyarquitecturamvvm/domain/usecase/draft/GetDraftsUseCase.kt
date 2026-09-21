package com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft

import com.example.firebaseroomyarquitecturamvvm.domain.model.TaskDraft
import com.example.firebaseroomyarquitecturamvvm.domain.repository.DraftRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDraftsUseCase @Inject constructor(
    private val draftRepository: DraftRepository
) {
    operator fun invoke(userId: String): Flow<List<TaskDraft>> {
        return draftRepository.observeDrafts(userId)
    }
}