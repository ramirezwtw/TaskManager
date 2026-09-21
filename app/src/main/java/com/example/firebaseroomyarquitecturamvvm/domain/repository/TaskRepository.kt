package com.example.firebaseroomyarquitecturamvvm.domain.repository

import com.example.firebaseroomyarquitecturamvvm.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(userId: String): Flow<List<Task>>
    suspend fun createTask(task: Task): Result<String>
    suspend fun updateTask(task: Task): Result<Unit>
    suspend fun deleteTask(taskId: String): Result<Unit>
}