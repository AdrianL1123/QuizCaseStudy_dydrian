package com.dydrian.quizCaseStudy.ui.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.dydrian.quizCaseStudy.R
import com.dydrian.quizCaseStudy.data.model.Question
import com.dydrian.quizCaseStudy.databinding.FragmentDetailsBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()
    override val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizId = args.quizId
        viewModel.getQuizById(quizId)

        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                quiz?.let {
                    binding.tvTitle.text = quiz.title
                    binding.tvTimePerQuestion.text =
                        "Timer per question: ${quiz.timePerQuestion.toString()} seconds"
                    binding.tvTotalQuestions.text = "Number of questions: ${quiz.questions?.size}"
                    binding.tvQuestionsList.text = formatQuestions(quiz.questions)
                    Log.d("debugging", "Questions: ${quiz.questions}")
                }
            }
        }
    }

    private fun formatQuestions(questions: List<Question>): String {
        return questions.joinToString("\n\n") { question ->
            var result = "Q: ${question.questionText}\n"
            question.options.forEach { option ->
                result += "• $option\n"
            }
            result += "Correct Answer: ${question.correctAnswer}\n"
            result
        }
    }

}
