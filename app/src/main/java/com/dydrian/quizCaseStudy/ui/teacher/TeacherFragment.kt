package com.dydrian.quizCaseStudy.ui.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.databinding.FragmentTeacherBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import com.dydrian.quizCaseStudy.ui.teacher.adapter.QuizAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherFragment : BaseFragment() {
    private lateinit var binding: FragmentTeacherBinding
    private lateinit var adapter: QuizAdapter
    override val viewModel: TeacherViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        lifecycleScope.launch {
            viewModel.quizzes.collect {
                binding.tvEmpty.visibility =
                    if (it.isEmpty()) View.VISIBLE else View.GONE

                adapter.setQuizzes(it)
            }
        }

        binding.ivUserProfile.setOnClickListener {
            findNavController().navigate(
                TeacherFragmentDirections
                    .actionTeacherFragmentToProfileFragment()
            )
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(
                TeacherFragmentDirections
                    .actionTeacherFragmentToTeacherAddQuizFragment()
            )
        }
    }

    private fun setupAdapter() {
        adapter = QuizAdapter(emptyList())
        binding.rvQuizzes.adapter = adapter
        binding.rvQuizzes.layoutManager = LinearLayoutManager(
            requireContext()
        )
    }
}