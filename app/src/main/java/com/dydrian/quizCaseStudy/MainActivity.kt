package com.dydrian.quizCaseStudy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.dydrian.quizCaseStudy.core.service.AuthService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        lifecycleScope.launch {
            val startDestination = when (
                val role = authService.getUserRole()
            ) {
                "Teacher" -> R.id.teacherFragment
                "Student" -> R.id.studentFragment
                null -> R.id.loginFragment
                else -> {
                    Log.e("MainActivity", "Unknown role: $role")
                    R.id.loginFragment
                }
            }

            navGraph.setStartDestination(startDestination)
            navController.setGraph(navGraph, null)
        }
    }
}