package com.dydrian.quizCaseStudy.ui.auth.signup

import androidx.lifecycle.viewModelScope
import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.ui.auth.base.BaseAuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authService: AuthService
) : BaseAuthViewModel() {
    fun register(email: String, password: String, role: String) {
        viewModelScope.launch {
            errorHandler {
                require(
                    email.isNotEmpty() &&
                            password.isNotEmpty() &&
                            role.isNotEmpty()
                ) { "All fields must be entered." }
                authService.register(email, password, role)
                _success.emit(Unit)
            }
        }
    }

    suspend fun getLoggedInUserRole(): String? {
        val user = authService.getLoggedInUser()
        return user?.uid?.let { authService.getUserRole() }
    }
}