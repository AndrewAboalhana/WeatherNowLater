package com.aa.search.viewmodel

import com.aa.common.base.BaseUiState

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSearchSuccessful: Boolean = false
) : BaseUiState
