package com.dydrian.quizCaseStudy.core.di

import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.core.service.AuthServiceImpl
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
    fun provideAuthService(): AuthService {
        return AuthServiceImpl()
    }
}