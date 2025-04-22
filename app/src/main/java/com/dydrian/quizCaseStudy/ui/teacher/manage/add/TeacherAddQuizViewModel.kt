package com.dydrian.quizCaseStudy.ui.teacher.manage.add

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dydrian.quizCaseStudy.data.model.Question
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.data.repo.QuizRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class TeacherAddQuizViewModel @Inject constructor(
    private val repo: QuizRepo
) : ViewModel() {
    private var _parsedQuestions = MutableStateFlow<List<Question>>(emptyList())
    val parsedQuestions = _parsedQuestions.asStateFlow()

    /**
     * Parses the CSV file from the given URI and updates the parsed questions list.
     * This function runs in the IO thread to handle file reading asynchronously.
     *
     * @param context The application context to access content resolver for opening the input stream.
     * @param uri The URI of the CSV file to be parsed.
     */
    fun parseCsv(context: Context, uri: Uri, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val questions = parseCsvToQuestions(reader)
                _parsedQuestions.value = questions
                Log.d("debugging", questions.toString())
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to parse CSV")
            }
        }
    }


    suspend fun addQuiz(quiz: Quiz) {
        repo.addQuiz(quiz)
    }

    /**
     * Processes each line of the CSV file read by the reader (BufferedReader)
     * and converts it into a list of questions
     */
    fun parseCsvToQuestions(reader: BufferedReader): List<Question> {
        val questions = mutableListOf<Question>()
        reader.readLine() // skip header line
        reader.forEachLine { line ->
            val tokens = line.split(",")
            if (tokens.size >= 6) {
                val questionText = tokens[0].trim()
                val options = tokens.subList(1, 5).map { it.trim() }
                val correctAnswer = tokens[5].trim()
                questions.add(Question(questionText, options, correctAnswer))
            }
        }
        return questions
    }
}