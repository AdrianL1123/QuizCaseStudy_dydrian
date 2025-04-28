package com.dydrian.quizCaseStudy.ui.student.result

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dydrian.quizCaseStudy.databinding.FragmentResultBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultFragment : BaseFragment() {
    private lateinit var binding: FragmentResultBinding
    override val viewModel: ResultViewModel by viewModels()
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvScore.text = args.score
        binding.buttonBackHome.setOnClickListener {
            findNavController()
                .navigate(
                    ResultFragmentDirections
                        .actionResultFragmentToStudentFragment()
                )
        }
        lifecycleScope.launch {
            // Collect all quiz scores using the quizId from args
            viewModel.quizScores.collect { scores ->
                val user = viewModel.getLoggedInUser()
                // Sort scores in descending order to find the rank
                val sortedScores = scores.sortedByDescending { it.score.toInt() }

                // Find the rank of the current user
                val userRank = sortedScores.indexOfFirst { it.user_email == user?.email }

                if (userRank != -1) {
                    // Rank is 1-based, so add 1 to the index
                    val rank = userRank + 1
                    binding.tvRank.text = "Rank: $rank out of ${sortedScores.size} Students"
                } else {
                    binding.tvRank.text = "Rank: No Rank Yet"
                }
            }
        }
    }
}