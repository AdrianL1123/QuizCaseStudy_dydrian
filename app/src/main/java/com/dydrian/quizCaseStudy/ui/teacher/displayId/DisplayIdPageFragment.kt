package com.dydrian.quizCaseStudy.ui.teacher.displayId

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dydrian.quizCaseStudy.core.showToast
import com.dydrian.quizCaseStudy.databinding.FragmentDisplayIdPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayIdPageFragment : Fragment() {
    private lateinit var binding: FragmentDisplayIdPageBinding
    private val args: DisplayIdPageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisplayIdPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvQuizId.text = args.quizId

        binding.btnCopyQuizId.setOnClickListener {
            val quizIdText = binding.tvQuizId.text.toString()

            // Get the system clipboard service to handle copying text
            val copyText =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // Create a plain text clip containing the quiz ID
            val copiedText = ClipData.newPlainText("Quiz ID", quizIdText)

            // Set the clip data to the system clipboard for copying
            copyText.setPrimaryClip(copiedText)
            showToast(requireContext(), "Quiz Id copied")
        }

        binding.btnBackToHome.setOnClickListener {
            findNavController().navigate(
                DisplayIdPageFragmentDirections.actionDisplayIdPageFragmentToTeacherFragment()
            )
        }
    }
}