package com.dydrian.quizCaseStudy.ui.auth.login


import androidx.lifecycle.viewModelScope
import com.dydrian.quizCaseStudy.core.CustomException
import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.ui.auth.base.BaseAuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : BaseAuthViewModel() {
    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                if (email.isEmpty() && password.isEmpty()) {
                    throw CustomException("Fields cannot be empty")
                }
                val res = authService.login(email, password)
                if (res) {
                    _success.emit(Unit)
                }
            }
        }
    }

    suspend fun getLoggedInUserRole(): String? {
        val user = authService.getLoggedInUser()
        return user?.uid?.let { authService.getUserRole() }
    }
}