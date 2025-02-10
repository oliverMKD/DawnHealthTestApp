package com.oliver.dawnhealthtestapp.presentation.model

sealed class GitHubRepositoryEvent {
    data class OnSearch(val query : String) : GitHubRepositoryEvent()
    data class OnDetails(val owner : String, val repoName : String) : GitHubRepositoryEvent()
    object OnErrorSeen : GitHubRepositoryEvent()
}