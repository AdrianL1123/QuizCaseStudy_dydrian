package com.dydrian.quizCaseStudy.core.di

import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.core.service.AuthServiceImpl
import com.dydrian.quizCaseStudy.data.repo.QuizRepo
import com.dydrian.quizCaseStudy.data.repo.QuizRepoFireStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideQuizRepo(): QuizRepo {
        return QuizRepoFireStoreImpl()
    }

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthServiceImpl()
    }
}