package com.dydrian.quizCaseStudy.ui.teacher

import androidx.lifecycle.viewModelScope
import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.data.repo.QuizRepo
import com.dydrian.quizCaseStudy.ui.base.BaseViewModel
import com.dydrian.quizCaseStudy.ui.teacher.adapter.QuizAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val authService: AuthService,
    private val repo: QuizRepo
) : BaseViewModel() {

    val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes = _quizzes.asStateFlow()

    init {
        getQuizzes()
    }

    private fun getQuizzes() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                repo.getQuizzes().collect { items ->
                    _quizzes.update {
                        items
                    }
                }
            }
        }
    }

}