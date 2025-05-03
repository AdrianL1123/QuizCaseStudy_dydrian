package com.dydrian.quizCaseStudy.ui.student.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.data.model.Score
import com.dydrian.quizCaseStudy.data.repo.QuizRepo
import com.dydrian.quizCaseStudy.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repo: QuizRepo,
    private val authService: AuthService,
    args: SavedStateHandle
) : BaseViewModel() {
    val quizId = args.get<String>("quizId") ?: "-1"

    val _quizScores = MutableStateFlow<List<Score>>(emptyList())
    val quizScores = _quizScores.asStateFlow()

    init {
        getQuizScores()
    }

    private fun getQuizScores() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                repo.getScoresForQuiz(quizId).collect { items ->
                    _quizScores.update {
                        items
                    }
                }
            }
        }
    }

    fun getLoggedInUser(): FirebaseUser? {
        return authService.getLoggedInUser()
    }
}