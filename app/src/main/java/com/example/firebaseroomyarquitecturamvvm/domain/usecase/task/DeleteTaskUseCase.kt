package com.example.firebaseroomyarquitecturamvvm.domain.usecase.task

import com.example.firebaseroomyarquitecturamvvm.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String): Result<Unit> {
        return taskRepository.deleteTask(taskId)
    }
}