package com.example.firebaseroomyarquitecturamvvm.domain.usecase.task

import com.example.firebaseroomyarquitecturamvvm.domain.model.Task
import com.example.firebaseroomyarquitecturamvvm.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        return taskRepository.updateTask(task)
    }
}