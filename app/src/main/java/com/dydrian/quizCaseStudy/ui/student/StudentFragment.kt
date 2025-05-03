package com.dydrian.quizCaseStudy.ui.student

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dydrian.quizCaseStudy.core.showToast
import com.dydrian.quizCaseStudy.databinding.FragmentStudentBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class StudentFragment : BaseFragment() {
    override val viewModel: StudentViewModel by viewModels()
    lateinit var binding: FragmentStudentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivUserProfile.setOnClickListener {
            findNavController().navigate(
                StudentFragmentDirections
                    .studentFragmentToProfileFragment()
            )
        }
        binding.btnJoinQuiz.setOnClickListener {
            val quizId = binding.etQuizId.text.toString().trim()

            if (quizId.isEmpty()) {
                showToast(requireContext(), "Quiz ID must be filled in")
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val quizExists = checkQuizId(quizId)
                if (!quizExists) {
                    showToast(requireContext(), "Quiz ID does not exist")
                    return@launch
                }
                findNavController().navigate(
                    StudentFragmentDirections.studentToQuiz(quizId)
                )
            }
        }
    }

    private suspend fun checkQuizId(id: String): Boolean {
        val document = Firebase.firestore.collection("quiz").document(id).get().await()
        return document.exists()
    }
}
