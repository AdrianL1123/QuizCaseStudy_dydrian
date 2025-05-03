package com.dydrian.quizCaseStudy.ui.teacher.manage.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dydrian.quizCaseStudy.core.showToast
import com.dydrian.quizCaseStudy.databinding.FragmentTeacherManageQuizBinding
import com.dydrian.quizCaseStudy.ui.teacher.manage.TeacherManageQuizFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherEditQuizFragment : TeacherManageQuizFragment() {
    private val viewModel: EditQuizViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherManageQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvManageTitle.text = "Edit Quiz"
        binding.btnManageCreateEdit.text = "Update Quiz"
        binding.llCsvUpload.visibility = View.GONE

        binding.btnManageCreateEdit.setOnClickListener {
            val title = binding.etQuizTitle.text.toString()
            val timePerQuestion = binding.etTimePerQuestion.text.toString().toIntOrNull()
            if (title.isEmpty()) {
                showToast(requireContext(), "Title cannot be empty")
                return@setOnClickListener
            }
            if (timePerQuestion == null || timePerQuestion < 5) {
                showToast(requireContext(), "Time limit must be 5 seconds or more")
                return@setOnClickListener
            }
            viewModel.updateQuiz(title, timePerQuestion)
            findNavController().popBackStack()
            showToast(requireContext(), "Quiz updated successfully")
        }
        observeQuiz()
    }

    private fun observeQuiz() {
        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                binding.etQuizTitle.setText(quiz?.title)
                binding.etTimePerQuestion.setText(quiz?.timePerQuestion?.toString() ?: "")
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                showToast(requireContext(), errorMessage)
            }
        }

    }
}
