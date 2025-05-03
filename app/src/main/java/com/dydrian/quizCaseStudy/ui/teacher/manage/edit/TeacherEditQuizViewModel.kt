package com.dydrian.quizCaseStudy.ui.teacher.manage.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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
class EditQuizViewModel @Inject constructor(
    private val repo: QuizRepo,
    args: SavedStateHandle
) : BaseViewModel() {
    private val quizId = args.get<String>("quizId") ?: "-1"

    private val _quiz = MutableStateFlow<Quiz?>(null)
    val quiz = _quiz.asStateFlow()

    init {
        getQuizById()
    }

    private fun getQuizById() {
        viewModelScope.launch {
            errorHandler {
                repo.getQuizById(quizId)
            }?.let { item ->
                _quiz.update { item }
            }
        }
    }

    fun updateQuiz(title: String, timePerQuestion: Int?) {
        viewModelScope.launch {
            errorHandler {
                val currentQuiz = _quiz.value
                val updatedQuiz =
                    currentQuiz?.copy(title = title, timePerQuestion = timePerQuestion)

                if (updatedQuiz != null) {
                    repo.updateQuiz(updatedQuiz)
                }
                _quiz.value = updatedQuiz
            }
        }
    }
}

