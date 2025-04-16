package com.dydrian.quizCaseStudy.ui.auth.login


import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dydrian.quizCaseStudy.ui.auth.base.BaseAuthFragment
import com.dydrian.quizCaseStudy.ui.auth.signup.SignupFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseAuthFragment() {
    override val viewModel: LoginViewModel by viewModels()

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.btnGoToSignupOrLogin.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }

        binding.tvSignupOrLogin.text = "Login"
        binding.tvAccStatus.text = "Don't have an Account yet?"
        binding.btnGoToSignupOrLogin.text = "Signup here"

        binding.textInputLayoutRole.visibility = View.GONE

        binding.btnAuth.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            viewModel.login(email, password)
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