package com.example.firebaseroomyarquitecturamvvm.domain.usecase.draft

import com.example.firebaseroomyarquitecturamvvm.domain.repository.DraftRepository
import javax.inject.Inject

class DeleteDraftUseCase @Inject constructor(
    private val draftRepository: DraftRepository
) {
    suspend operator fun invoke(draftId: Int): Result<Unit> {
        return draftRepository.deleteDraft(draftId)
    }
}