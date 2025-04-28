package com.dydrian.quizCaseStudy.ui.teacher.manage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dydrian.quizCaseStudy.core.showToast
import com.dydrian.quizCaseStudy.data.model.Quiz
import com.dydrian.quizCaseStudy.data.repo.QuizRepo
import com.dydrian.quizCaseStudy.databinding.FragmentTeacherManageQuizBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import kotlinx.coroutines.launch

abstract class TeacherManageQuizFragment : Fragment() {
    protected lateinit var binding: FragmentTeacherManageQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherManageQuizBinding.inflate(inflater, container, false)
        return binding.root
    }
}