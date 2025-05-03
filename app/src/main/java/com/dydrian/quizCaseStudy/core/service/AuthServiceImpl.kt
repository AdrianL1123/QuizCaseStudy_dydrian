package com.dydrian.quizCaseStudy.core.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class AuthServiceImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthService {

    override suspend fun login(email: String, password: String): Boolean {
        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return authResult.user != null
    }

    override suspend fun register(email: String, password: String, role: String): Boolean {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user

        user?.let {
            val userData = mapOf(
                "email" to email,
                "role" to role
            )
            firestore
                .collection("users")
                .document(it.uid)
                .set(userData)
                .await()
            return true
        }
        return false
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getLoggedInUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun getUid(): String? {
        return firebaseAuth.uid
    }

    override suspend fun getUserRole(): String? {
        val snapshot =
            firebaseAuth.uid?.let {
                firestore
                    .collection("users")
                    .document(it)
                    .get()
                    .await()
            }
        return snapshot?.getString("role")
    }
}