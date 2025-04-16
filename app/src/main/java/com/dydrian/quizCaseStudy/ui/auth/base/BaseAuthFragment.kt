package com.dydrian.quizCaseStudy.ui.auth.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dydrian.quizCaseStudy.databinding.FragmentBaseAuthBinding
import com.dydrian.quizCaseStudy.ui.base.BaseFragment
import com.dydrian.quizCaseStudy.ui.base.BaseViewModel

abstract class BaseAuthFragment : BaseFragment() {
    protected lateinit var binding: FragmentBaseAuthBinding
    abstract override val viewModel: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseAuthBinding.inflate(inflater, container, false)
        return binding.root
    }
}