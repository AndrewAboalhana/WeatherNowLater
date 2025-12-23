package com.aa.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface BaseUiState

abstract class BaseViewModel<STATE : BaseUiState>(initialState: STATE) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    protected fun updateState(function: (STATE) -> STATE) {
        _state.update(function)
    }
}