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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private val _currentScore = MutableStateFlow(0)
    val currentScore = _currentScore.asStateFlow()

    val currentQuestionIndex = MutableStateFlow(0)
    val currentQuestion = MutableStateFlow<Question?>(null)

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft = _timeLeft.asStateFlow()

    private val _navigateToResult = MutableStateFlow("")
    val navigateToResult = _navigateToResult.asStateFlow()

    private var timerJob: Job? = null


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
                timer()
            }
        }
    }

    fun timer() {
        timerJob?.cancel() // cancel previous timer if any

        timerJob = viewModelScope.launch {
            val timePerQuestion = quiz.value?.timePerQuestion
            if (timePerQuestion != null) {
                _timeLeft.value = timePerQuestion
            }
            while (_timeLeft.value > 0) {
                delay(1000) // 1 sec
                _timeLeft.update { it - 1 }
            }
            // When time is up
            onTimeUp()
        }
    }

    // automatically move to next question
    private fun onTimeUp() {
        nextQuestion()
    }

    fun nextQuestion() {
        val quiz = _quiz.value ?: return
        val nextQuestionIndex = currentQuestionIndex.value + 1
        if (nextQuestionIndex < quiz.questions.size) {
            currentQuestionIndex.value = nextQuestionIndex
            currentQuestion.value = quiz.questions[nextQuestionIndex]
            timer()
        } else {
            timerJob?.cancel() // stop timer
            viewModelScope.launch {
                // pass the score to fragment so that I can navigate to result page
                _navigateToResult.emit("${currentScore.value}/${quiz.questions.size}")
            }
        }
    }


    fun checkAnswer(context: Context, selectedAnswer: String) {
        if (selectedAnswer.trim() == currentQuestion.value?.correctAnswer?.trim()) {
            _currentScore.value += 1
            showToast(
                context,
                "Answer Is Correct ! ${currentScore.value}/${quiz.value?.questions?.size}"
            )
        }
    }

    suspend fun storeScore() {
        repo.storeScore(quizId, currentScore.value.toString())
    }
}