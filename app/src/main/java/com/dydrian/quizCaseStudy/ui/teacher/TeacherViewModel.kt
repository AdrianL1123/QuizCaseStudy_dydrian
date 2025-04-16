package com.dydrian.quizCaseStudy.ui.teacher

import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val authService: AuthService
) : BaseViewModel() {

    fun logout() {
        return authService.logout()
    }
}