package com.dydrian.quizCaseStudy.ui.teacher.details

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.dydrian.quizCaseStudy.core.showToast
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.data.repo.QuizRepo
import com.dydrian.quizCaseStudy.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: QuizRepo
) : BaseViewModel() {
    private val _quiz = MutableStateFlow<Quiz?>(null)
    val quiz = _quiz.asStateFlow()

    fun getQuizById(context: Context, id: String) {
        viewModelScope.launch {
            try {
                val result = repo.getQuizById(id)
                _quiz.value = result
            } catch (e: Exception) {
                showToast(context,e.message ?: "Failed to get quiz")
                _quiz.value = null
            }
        }
    }

    fun deleteQuiz(id: String) {
        viewModelScope.launch {
            errorHandler {
                repo.deleteQuiz(id)
            }
        }
    }
}