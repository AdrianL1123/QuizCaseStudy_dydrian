package com.dydrian.quizCaseStudy.ui.student.quizConfirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dydrian.quizCaseStudy.databinding.FragmentQuizConfirmationBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizConfirmationFragment : BaseFragment() {
    private lateinit var binding: FragmentQuizConfirmationBinding
    override val viewModel: QuizConfirmationViewModel by viewModels()
    private val args: QuizConfirmationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.joinedQuiz.collect {
                binding.tvQuizTitle.text = it?.title
                if (it?.timePerQuestion != null) {
                    binding.tvTimePerQuestion.text = "Time Per Question: ${it?.timePerQuestion}s"
                } else {
                    binding.tvTimePerQuestion.visibility = View.GONE
                }

                binding.tvNumberOfQuestions.text = "Number Of Questions: ${it?.questions?.size}"
            }
        }
        binding.btnStartQuiz.setOnClickListener {
            findNavController().navigate(
                QuizConfirmationFragmentDirections
                    .actionQuizConfirmationFragmentToTakeQuizFragment(args.quizId)
            )
        }

    }
}