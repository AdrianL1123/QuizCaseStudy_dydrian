package com.dydrian.quizCaseStudy.core.service

import com.google.firebase.auth.FirebaseUser

interface AuthService {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(email: String, password: String, role: String): Boolean
    fun logout()
    fun getLoggedInUser(): FirebaseUser?
    fun getUid(): String?
    suspend fun getUserRole(): String?
}