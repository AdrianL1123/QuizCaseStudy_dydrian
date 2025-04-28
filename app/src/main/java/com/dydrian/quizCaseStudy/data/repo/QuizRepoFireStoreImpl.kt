package com.dydrian.quizCaseStudy.data.repo

import com.dydrian.quizCaseStudy.core.service.AuthService
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.data.model.Score
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
        return db.collection("quiz")
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
        val docRef = getCollectionRef().document(quiz.id!!)
        docRef.set(quiz).await()
    }

    override suspend fun getQuizById(id: String): Quiz? {
        val snapshot = getCollectionRef().document(id).get().await()
        return snapshot.toObject(Quiz::class.java)?.copy(id = snapshot.id)
    }

    // Store the score for a student in a particular quiz
    override suspend fun storeScore(quizId: String, score: String) {
        val user = authService.getLoggedInUser()
        val scoreData = mapOf(
            "score" to score,
            "user_email" to user?.email
        )

        // Store the score inside the quiz
        if (user != null) {
            getCollectionRef().document(quizId)
                .collection("scores")
                .document(user.uid)
                .set(scoreData)
                .await()
        }
    }

    override suspend fun getScoresForQuiz(quizId: String): Flow<List<Score>> = callbackFlow {
        val listener = getCollectionRef().document(quizId)
            .collection("scores")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val scores = mutableListOf<Score>()
                value?.documents?.forEach { doc ->
                    doc.toObject(Score::class.java)?.let { score ->
                        scores.add(score)
                    }
                }
                trySend(scores)
            }
        awaitClose { listener.remove() }
    }
}