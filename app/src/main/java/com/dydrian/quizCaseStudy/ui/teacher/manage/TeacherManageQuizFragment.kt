package com.dydrian.quizCaseStudy.ui.teacher.manage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dydrian.quizCaseStudy.databinding.FragmentTeacherManageQuizBinding

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