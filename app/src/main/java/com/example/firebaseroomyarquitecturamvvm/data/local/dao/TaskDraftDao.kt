package com.example.firebaseroomyarquitecturamvvm.data.local.dao

import androidx.room.*
import com.example.firebaseroomyarquitecturamvvm.data.local.entity.TaskDraftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDraftDao {
    @Query("SELECT * FROM task_drafts WHERE ownerId = :userId ORDER BY savedAt DESC")
    fun observeDrafts(userId: String): Flow<List<TaskDraftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: TaskDraftEntity)

    @Update
    suspend fun updateDraft(draft: TaskDraftEntity)

    @Query("DELETE FROM task_drafts WHERE id = :draftId")
    suspend fun deleteDraft(draftId: Int)
}