package com.oliver.dawnhealthtestapp.presentation.model

import androidx.compose.runtime.Stable
import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.domain.util.GitHubRepositoriesErrors

@Stable
data class HomeUiState(
    val list: List<GithubRepository> = emptyList(),
    val count: Int = 0,
    val searchQuery: String = "",
    val error: GitHubRepositoriesErrors? = null,
    val isSearching : Boolean = false,
    val releaseDate : String = ""
)
