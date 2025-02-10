package com.oliver.dawnhealthtestapp.domain.repository

import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.domain.model.RepositoryReleaseVersion
import kotlinx.coroutines.flow.Flow

interface GitHubRepoRepository {
    suspend fun searchGitHubRepositories(searchQuery: String) : Flow<Result<Pair<Int,List<GithubRepository>>>>
    suspend fun getRepositoryLastReleaseVersion(repo : String, owner : String) : Flow<Result<List<RepositoryReleaseVersion>>>
}