package com.dydrian.quizCaseStudy.ui.teacher.manage.add

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dydrian.quizCaseStudy.core.showToast
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.databinding.FragmentTeacherManageQuizBinding
import com.dydrian.quizCaseStudy.ui.teacher.manage.TeacherManageQuizFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class TeacherAddQuizFragment : TeacherManageQuizFragment() {
    private val viewModel: TeacherAddQuizViewModel by viewModels()

    /**
     * Launches the Storage Access Framework (SAF) file picker to select a CSV file.
     *
     * Uses ActivityResultContracts.GetContent() to allow the user to choose a file.
     */
    private val csvPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
        // to get uri/content->(from the file user chose)
    ) { uri ->
        uri?.let {
            val fileName = getFileName(uri)
            if (fileName != null && !fileName.endsWith(".csv", ignoreCase = true)) {
                showToast(requireContext(), "Invalid file selected")
                return@let
            }

            viewModel.parseCsv(
                context = requireContext(),
                uri = it,
                onSuccess = {
                    binding.tvCsvFileName.text = fileName
                    showToast(requireContext(), "CSV Loaded Successfully")
                },
                onError = { error ->
                    showToast(requireContext(), "Error: $error")
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherManageQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnUploadCsv.setOnClickListener {
            csvPickerLauncher.launch("*/*")
        }
        binding.btnManageCreateEdit.setOnClickListener {
            val title = binding.etQuizTitle.text.toString()
            val time = binding.etTimePerQuestion.text.toString().toIntOrNull()
            val questions = viewModel.parsedQuestions.value

            if (title.isEmpty() || questions.isEmpty()) {
                showToast(requireContext(), "Title and Questions field cannot be empty.")
                return@setOnClickListener
            }

            if (time == null || time < 10) {
                showToast(requireContext(), "Time limit must be at least 10 seconds.")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val quiz = Quiz(
                    id = generateUniqueQuizId(),
                    title = title,
                    timePerQuestion = time,
                    questions = questions
                )
                viewModel.addQuiz(quiz)
                findNavController().navigate(
                    TeacherAddQuizFragmentDirections
                        .actionTeacherAddQuizFragmentToDisplayIdPageFragment(quiz.id!!)
                )
                showToast(requireContext(), "Quiz Added")
            }
        }
    }

    /**
     * get actual file name with uri
     */
    private fun getFileName(uri: Uri): String? {
        // This queries the content provider associated with the uri and returns a Cursor.
        // A Cursor is like a pointer to a table of results.
        val cursor = requireContext().contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            // This gets the index of the “DISPLAY_NAME” column,
            // which stores the file name (like "questions.csv").
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            // if return -1 prevent crash here
            if (nameIndex != -1 && it.moveToFirst()) {
                return it.getString(nameIndex)
            }
        }
        return null
    }

    /**
     * generate unique quiz id
     */
    private suspend fun generateUniqueQuizId(): String {
        val characters = ('A'..'Z') + ('0'..'9')
        val collection = Firebase.firestore.collection("quiz")
        while (true) {
            val id = (1..6).map { characters.random() }.joinToString("")
            val exists = collection.document(id).get().await().exists()
            if (!exists) return id
        }
    }
}