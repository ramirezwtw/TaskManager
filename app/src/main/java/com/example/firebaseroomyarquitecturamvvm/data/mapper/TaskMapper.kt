package com.example.firebaseroomyarquitecturamvvm.data.mapper

import com.example.firebaseroomyarquitecturamvvm.data.local.entity.TaskDraftEntity
import com.example.firebaseroomyarquitecturamvvm.data.remote.model.TaskDocument
import com.example.firebaseroomyarquitecturamvvm.domain.model.Task
import com.example.firebaseroomyarquitecturamvvm.domain.model.TaskDraft

fun TaskDocument.toDomain(): Task = Task(
    id = id,
    ownerId = ownerId,
    title = title,
    description = description,
    completed = completed,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Task.toDocument(): TaskDocument = TaskDocument(
    id = id,
    ownerId = ownerId,
    title = title,
    description = description,
    completed = completed,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun TaskDraftEntity.toDomain(): TaskDraft = TaskDraft(
    id = id,
    ownerId = ownerId,
    title = title,
    description = description,
    savedAt = savedAt
)

fun TaskDraft.toEntity(): TaskDraftEntity = TaskDraftEntity(
    id = id,
    ownerId = ownerId,
    title = title,
    description = description,
    savedAt = savedAt
)