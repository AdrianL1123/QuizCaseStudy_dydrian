package com.dydrian.quizCaseStudy.ui.student.takeQuiz

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dydrian.quizCaseStudy.core.showToast
import com.dydrian.quizCaseStudy.data.model.Question
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.data.repo.QuizRepo
import com.dydrian.quizCaseStudy.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakeQuizViewModel @Inject constructor(
    private val repo: QuizRepo,
    args: SavedStateHandle
) : BaseViewModel() {
    val quizId = args.get<String>("quizId") ?: "-1"

    private val _quiz = MutableStateFlow<Quiz?>(null)
    val quiz = _quiz.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    val currentQuestionIndex = MutableStateFlow(0)
    val currentQuestion = MutableStateFlow<Question?>(null)


    init {
        getQuizById()
    }

    private fun getQuizById() {
        viewModelScope.launch {
            errorHandler {
                repo.getQuizById(
                    quizId.trim()
                )
            }?.let { items ->
                _quiz.update { items }
                currentQuestionIndex.update { 0 }
                currentQuestion.update { items.questions.firstOrNull() }
            }
        }
    }

    fun nextQuestion() {
        val quiz = _quiz.value ?: return
        val nextQuestionIndex = currentQuestionIndex.value + 1
        if (nextQuestionIndex < quiz.questions.size) {
            currentQuestionIndex.value = nextQuestionIndex
            currentQuestion.value = quiz.questions[nextQuestionIndex]
        } else {
            // Todo get to results page
        }
    }

    fun timer() {
        TODO()
    }

    fun checkAnswer(context: Context, selectedAnswer: String) {
        if (selectedAnswer.trim() == currentQuestion.value?.correctAnswer?.trim()) {
            _score.value += 1
            showToast(
                context,
                "Answer Is Correct ! ${score.value}/${quiz.value?.questions?.size}"
            )
        }
    }
}