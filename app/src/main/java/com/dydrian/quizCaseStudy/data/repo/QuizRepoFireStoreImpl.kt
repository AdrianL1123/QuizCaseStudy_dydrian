package com.dydrian.quizCaseStudy.data.repo

import com.dydrian.quizCaseStudy.core.CustomException
import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuizRepoFireStoreImpl(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val authService: AuthService
) : QuizRepo {
    private fun getCollectionRef(): CollectionReference {
        val uid = authService.getUid()
            ?: throw CustomException("User Not Found")
        return db.collection("users/$uid/quiz")
    }

    override fun getQuizzes(): Flow<List<Quiz>> = callbackFlow {
        val listener = getCollectionRef().addSnapshotListener { value, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val quizzes = mutableListOf<Quiz>()
            value?.documents?.forEach { doc ->
                doc.toObject(Quiz::class.java)?.let { quiz ->
                    quizzes.add(quiz.copy(id = doc.id))
                }
            }
            trySend(quizzes)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun addQuiz(quiz: Quiz) {
        val docRef = getCollectionRef().document()
        docRef.set(quiz.copy(id = docRef.id)).await()
    }

    override suspend fun getQuizById(id: String): Quiz? {
        val snapshot = getCollectionRef().document(id).get().await()
        return snapshot.toObject(Quiz::class.java)?.copy(id = snapshot.id)
    }
}