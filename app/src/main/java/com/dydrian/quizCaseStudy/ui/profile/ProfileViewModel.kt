package com.dydrian.quizCaseStudy.ui.profile

import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authService: AuthService
) :
    BaseViewModel() {
    suspend fun getUserRole(): String? {
        return authService.getUserRole()
    }

    fun getLoggedInUser(): FirebaseUser? {
        return authService.getLoggedInUser()
    }


    fun logout() {
        return authService.logout()
    }
}