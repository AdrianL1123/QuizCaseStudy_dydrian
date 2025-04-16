package com.dydrian.quizCaseStudy.ui.auth.signup

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dydrian.quizCaseStudy.ui.auth.base.BaseAuthFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : BaseAuthFragment() {
    override val viewModel: SignupViewModel by viewModels()

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.btnGoToSignupOrLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvSignupOrLogin.text = "Signup"
        binding.tvAccStatus.text = "Already have an Account?"
        binding.btnGoToSignupOrLogin.text = "Login here"

        binding.textInputLayoutRole.visibility = View.VISIBLE
        binding.autoCompleteRole.keyListener = null

        val roles = listOf("Teacher", "Student")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, roles)
        binding.autoCompleteRole.setAdapter(adapter)

        binding.autoCompleteRole.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position).toString()
            Log.d("debugging", "Selected Role: $selected")
        }

        binding.btnAuth.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val selectedRole = binding.autoCompleteRole.text.toString()
            Log.d("debugging", selectedRole)
            viewModel.register(email, password, selectedRole)
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)
        lifecycleScope.launch {
            viewModel.success.collect {
                viewModel.getLoggedInUserRole().let { role ->
                    when (role) {
                        "Teacher" -> {
                            val action =
                                SignupFragmentDirections.actionLoginFragmentToTeacherFragment()
                            findNavController().navigate(action)
                        }

                        "Student" -> {
                            val action =
                                SignupFragmentDirections.actionLoginFragmentToStudentFragment()
                            findNavController().navigate(action)
                        }

                        else -> {
                            Log.e("SignupFragment", "Unknown role: $role")
                        }
                    }
                }
            }
        }
    }
}