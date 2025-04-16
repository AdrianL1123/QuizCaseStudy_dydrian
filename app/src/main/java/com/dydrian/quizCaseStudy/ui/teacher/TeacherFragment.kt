package com.dydrian.quizCaseStudy.ui.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dydrian.quizCaseStudy.databinding.FragmentTeacherBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherFragment : BaseFragment() {
    private lateinit var binding: FragmentTeacherBinding
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
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController()
                .navigate(TeacherFragmentDirections.actionToLoginFragment())
        }
    }

}