package com.dydrian.quizCaseStudy.ui.student.quizConfirmation

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
class QuizConfirmationViewModel @Inject constructor(
    private val repo: QuizRepo,
    args: SavedStateHandle
) : BaseViewModel() {
    val quizId = args.get<String>("quizId") ?: "-1"
    private val _joinedQuiz = MutableStateFlow<Quiz?>(null)
    val joinedQuiz = _joinedQuiz.asStateFlow()

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
                _joinedQuiz.update { items }
            }
        }
    }
}