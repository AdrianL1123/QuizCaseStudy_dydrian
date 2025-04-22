package com.dydrian.quizCaseStudy.data.repo

import com.dydrian.quizCaseStudy.data.model.Quiz
import kotlinx.coroutines.flow.Flow


interface QuizRepo {
    fun getQuizzes(): Flow<List<Quiz>>
    suspend fun addQuiz(quiz: Quiz)
}