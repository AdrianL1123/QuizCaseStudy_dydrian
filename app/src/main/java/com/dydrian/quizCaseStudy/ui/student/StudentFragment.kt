package com.dydrian.quizCaseStudy.ui.student

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dydrian.quizCaseStudy.databinding.FragmentStudentBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

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

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController()
                .navigate(StudentFragmentDirections.actionToLoginFragment())
        }
    }


}