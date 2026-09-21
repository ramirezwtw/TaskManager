package com.example.firebaseroomyarquitecturamvvm.data.repository

import com.example.firebaseroomyarquitecturamvvm.data.mapper.toDocument
import com.example.firebaseroomyarquitecturamvvm.data.mapper.toDomain
import com.example.firebaseroomyarquitecturamvvm.data.remote.model.TaskDocument
import com.example.firebaseroomyarquitecturamvvm.domain.model.Task
import com.example.firebaseroomyarquitecturamvvm.domain.repository.TaskRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TaskRepository {
    private val tasksCollection = firestore.collection("tasks")

    override fun observeTasks(userId: String): Flow<List<Task>> = callbackFlow {
        val subscription = tasksCollection
            .whereEqualTo("ownerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val tasks = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(TaskDocument::class.java)?.toDomain()
                } ?: emptyList()
                trySend(tasks)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun createTask(task: Task): Result<String> {
        return try {
            val documentRef = tasksCollection.add(task.toDocument()).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            tasksCollection.document(task.id).set(task.toDocument()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            tasksCollection.document(taskId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}