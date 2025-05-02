package com.dydrian.quizCaseStudy.ui.teacher.manage.edit

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dydrian.quizCaseStudy.core.showToast
import com.dydrian.quizCaseStudy.data.model.Question
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.data.repo.QuizRepo
import com.dydrian.quizCaseStudy.ui.base.BaseViewModel
import com.dydrian.quizCaseStudy.ui.teacher.manage.TeacherManageQuizFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuizViewModel @Inject constructor(
    private val repo: QuizRepo,
    args: SavedStateHandle
) : BaseViewModel() {

    val quizId = args.get<String>("quizId") ?: "-1"

    private val _quiz = MutableStateFlow<Quiz?>(null)
    val quiz = _quiz.asStateFlow()

    init {
        getQuizById()
    }

    private fun getQuizById() {
        viewModelScope.launch {
            errorHandler{
                repo.getQuizById(quizId)
            }?.let { item ->
                Log.d("debugging", item.toString())
                _quiz.update { item }
            }
        }
    }

    fun updateQuiz(title: String, timePerQuestion: Int) {
        viewModelScope.launch {
            errorHandler {
                require(title.isNotEmpty()) { "Title cannot be empty." }
                require(timePerQuestion > 0) { "Time must be greater than zero." }

                val currentQuiz = _quiz.value ?: throw Exception("Quiz not found")
                val updatedQuiz = currentQuiz.copy(title = title, timePerQuestion = timePerQuestion)

                repo.updateQuiz(updatedQuiz)
                _quiz.value = updatedQuiz
            }
        }
    }


}

