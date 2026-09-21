package com.example.firebaseroomyarquitecturamvvm.domain.usecase.task

import com.example.firebaseroomyarquitecturamvvm.domain.model.Task
import com.example.firebaseroomyarquitecturamvvm.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(userId: String): Flow<List<Task>> {
        return taskRepository.observeTasks(userId)
    }
}