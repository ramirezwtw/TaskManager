package com.example.firebaseroomyarquitecturamvvm.di

import com.example.firebaseroomyarquitecturamvvm.data.repository.AuthRepositoryImpl
import com.example.firebaseroomyarquitecturamvvm.data.repository.DraftRepositoryImpl
import com.example.firebaseroomyarquitecturamvvm.data.repository.TaskRepositoryImpl
import com.example.firebaseroomyarquitecturamvvm.domain.repository.AuthRepository
import com.example.firebaseroomyarquitecturamvvm.domain.repository.DraftRepository
import com.example.firebaseroomyarquitecturamvvm.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindDraftRepository(impl: DraftRepositoryImpl): DraftRepository
}