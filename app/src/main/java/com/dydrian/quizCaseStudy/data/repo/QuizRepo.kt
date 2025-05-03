package com.dydrian.quizCaseStudy.data.repo

import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.data.model.Score
import kotlinx.coroutines.flow.Flow


interface QuizRepo {
    fun getQuizzes(): Flow<List<Quiz>>
    suspend fun addQuiz(quiz: Quiz)
    suspend fun getQuizById(id: String): Quiz?
    suspend fun updateQuiz(quiz: Quiz)
    suspend fun deleteQuiz(id: String)
    suspend fun storeScore(quizId: String, score: String)
    suspend fun getScoresForQuiz(quizId: String): Flow<List<Score>>
}