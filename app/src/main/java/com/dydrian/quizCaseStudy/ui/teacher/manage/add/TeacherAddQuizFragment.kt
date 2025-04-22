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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherAddQuizFragment : TeacherManageQuizFragment() {
    private val viewModel: TeacherAddQuizViewModel by viewModels()

    /**
     *   Storage Access Framework (SAF)
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
        binding.btnCreate.setOnClickListener {
            val title = binding.etQuizTitle.text.toString()
            val time = binding.etTimePerQuestion.text.toString().toIntOrNull()
            val questions = viewModel.parsedQuestions.value

            val quiz = Quiz(
                title = title,
                timePerQuestion = time,
                questions = questions
            )

            lifecycleScope.launch {
                viewModel.addQuiz(quiz)
                findNavController().popBackStack()
                showToast(requireContext(), "Quiz Added")
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && it.moveToFirst()) {
                return it.getString(nameIndex)
            }
        }
        return null
    }
}