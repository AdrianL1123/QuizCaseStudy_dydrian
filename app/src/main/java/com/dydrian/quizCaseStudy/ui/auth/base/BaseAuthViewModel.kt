package com.dydrian.quizCaseStudy.ui.auth.base

import com.dydrian.quizCaseStudy.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseAuthViewModel : BaseViewModel() {
    protected val _success = MutableSharedFlow<Unit>()
    val success = _success.asSharedFlow()
}