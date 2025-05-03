package com.dydrian.quizCaseStudy.ui.student.takeQuiz

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dydrian.quizCaseStudy.core.showToast
import com.dydrian.quizCaseStudy.databinding.FragmentTakeQuizBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TakeQuizFragment : BaseFragment() {
    private lateinit var binding: FragmentTakeQuizBinding
    override val viewModel: TakeQuizViewModel by viewModels()
    private var selectedAnswer: String? = null
    private val args: TakeQuizFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTakeQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            // get quiz
            viewModel.quiz.collect {
                binding.tvQuizTitle.text = it?.title
            }
        }

        // quiz question number
        lifecycleScope.launch {
            viewModel.currentQuestionIndex
                // collect question index and quiz use Combine
                .combine(viewModel.quiz) { index, quiz ->
                    Pair(index, quiz)
                }
                .collect { (index, quiz) ->
                    if (quiz != null && quiz.questions.isNotEmpty()) {
                        binding.tvNumberOfQuestions.text = "${index + 1}/${quiz.questions.size}"
                    }
                }
        }

        // timer
        lifecycleScope.launch {
            viewModel.timeLeft.collect { timeLeft ->
                if (timeLeft != null) {
                    binding.tvTimer.text = "00:$timeLeft"
                } else {
                    binding.tvTimer.visibility = View.GONE
                }
            }
        }

        // display current question and options
        lifecycleScope.launch {
            viewModel.currentQuestion.collect { question ->
                if (question != null) {
                    binding.tvQuestions.text = question.questionText
                    binding.rbAnswer1.text = question.options[0]
                    binding.rbAnswer2.text = question.options[1]
                    binding.rbAnswer3.text = question.options[2]
                    binding.rbAnswer4.text = question.options[3]
                }
            }
        }

        // results page
        lifecycleScope.launch {
            viewModel.navigateToResult.collect { score ->
                if (score != "") {
                    findNavController().navigate(
                        TakeQuizFragmentDirections
                            .takeQuizFragmentToResultFragment(
                                score, args.quizId
                            )
                    )
                    viewModel.storeScore()
                }
            }
        }


        // get answer from user
        binding.radioGroup
            .setOnCheckedChangeListener { group, checkedId ->
                if (checkedId != -1) {
                    val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
                    selectedAnswer = selectedRadioButton.text.toString()
                }
            }

        binding.btnNext.setOnClickListener {
            if (selectedAnswer == null) {
                return@setOnClickListener showToast(requireContext(), "Please select an answer!")
            }

            viewModel.checkAnswer(requireContext(), selectedAnswer!!)
            binding.radioGroup.clearCheck()
            selectedAnswer = null

            viewModel.nextQuestion()
        }
    }
}