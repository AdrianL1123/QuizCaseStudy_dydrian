package com.dydrian.quizCaseStudy.data.model

data class Quiz(
    val id: String? = null,
    val title: String = "",
    val timePerQuestion: Int? = null,
    val questions: List<Question> = emptyList()
)

data class Question(
    val questionText: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: String = ""
)