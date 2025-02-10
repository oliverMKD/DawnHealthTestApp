package com.oliver.dawnhealthtestapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliver.dawnhealthtestapp.domain.repository.GitHubRepoRepository
import com.oliver.dawnhealthtestapp.domain.util.GitHubRepositoriesErrors
import com.oliver.dawnhealthtestapp.domain.util.GithubRepoException
import com.oliver.dawnhealthtestapp.presentation.model.GitHubRepositoryEvent
import com.oliver.dawnhealthtestapp.presentation.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubRepositoriesViewModel @Inject constructor(
    private val repository: GitHubRepoRepository
) : ViewModel() {

    private val uiStateFlow = MutableStateFlow(HomeUiState())
    val stateFlow = uiStateFlow.asStateFlow()

    fun onEvent(gitHubRepositoryEvent: GitHubRepositoryEvent) {
        when (gitHubRepositoryEvent) {
            is GitHubRepositoryEvent.OnSearch -> {
                uiStateFlow.update {
                    it.copy(
                        searchQuery = gitHubRepositoryEvent.query,
                        isSearching = true
                    )
                }
                triggerSearch()
            }

            is GitHubRepositoryEvent.OnDetails -> {
                uiStateFlow.update { it.copy(isSearching = true) }
                getLatestRelease(
                    owner = gitHubRepositoryEvent.owner,
                    repo = gitHubRepositoryEvent.repoName
                )
            }
        }
    }

    private fun triggerSearch() {
        val query = uiStateFlow.value.searchQuery
        if (query.isBlank() || query.length <= 2) {
            uiStateFlow.update { it.copy(list = emptyList(), isSearching = false, error = null) }
            return
        }
        searchRepositories(query)
    }

    private fun searchRepositories(query: String) = viewModelScope.launch {
        repository.searchGitHubRepositories(query).collectLatest { result ->
            result.fold(
                onSuccess = { (totalResults, repos) ->
                    uiStateFlow.update {
                        it.copy(
                            list = repos,
                            count = totalResults,
                            isSearching = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    val repoError =
                        if (error is GithubRepoException) error.error else GitHubRepositoriesErrors.UNKNOWN_ERROR
                    uiStateFlow.update { it.copy(isSearching = false, error = repoError) }
                }
            )
        }
    }

    private fun getLatestRelease(owner: String, repo: String) = viewModelScope.launch {
        repository.getRepositoryLastReleaseVersion(repo, owner).collectLatest { result ->
            result.fold(
                onSuccess = { releases ->
                    val firstStableRelease = releases.firstOrNull { !it.prerelease }
                    uiStateFlow.update {
                        it.copy(
                            releaseDate = firstStableRelease?.tag_name ?: "",
                            isSearching = false
                        )
                    }
                },
                onFailure = { error ->
                    val repoError =
                        if (error is GithubRepoException) error.error else GitHubRepositoriesErrors.UNKNOWN_ERROR
                    uiStateFlow.update { it.copy(isSearching = false, error = repoError) }
                }
            )
        }
    }
}