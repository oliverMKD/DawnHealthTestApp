package com.oliver.dawnhealthtestapp.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oliver.dawnhealthtestapp.data.mappers.mapHttpErrorToGithubError
import com.oliver.dawnhealthtestapp.data.mappers.toGithubReleaseVersion
import com.oliver.dawnhealthtestapp.data.mappers.toGithubRepositoryItem
import com.oliver.dawnhealthtestapp.data.remote.GithubRepositoriesApi
import com.oliver.dawnhealthtestapp.data.remote.dto.GitHubRepositoryReleaseDTO
import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.domain.model.RepositoryReleaseVersion
import com.oliver.dawnhealthtestapp.domain.repository.GitHubRepoRepository
import com.oliver.dawnhealthtestapp.domain.util.GitHubRepositoriesErrors
import com.oliver.dawnhealthtestapp.domain.util.GithubRepoException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.lang.reflect.Type
import javax.inject.Inject

class GitHubRepoRepositoryImpl @Inject constructor(
    private val api: GithubRepositoriesApi,
    private val gson: Gson
) : GitHubRepoRepository {

    override suspend fun searchGitHubRepositories(searchQuery: String): Flow<Result<Pair<Int, List<GithubRepository>>>> =
        flow {
            handleApiResponse(
                call = { api.searchRepositories(query = searchQuery) },
                onSuccess = { response ->
                    val totalItems = response.totalCount.takeIf { it >= 0 } ?: 0
                    val repositories = response.items.map { it.toGithubRepositoryItem() }
                    Result.success(totalItems to repositories)
                }
            ).let {
                emit(it)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getRepositoryLastReleaseVersion(
        repo: String,
        owner: String
    ): Flow<Result<List<RepositoryReleaseVersion>>> =
        flow {
            handleApiResponse(
                call = { api.getRepositoryReleaseVersion(owner = owner, repo = repo) },
                onSuccess = { responseBody ->
                    val collectionType: Type = object : TypeToken<List<GitHubRepositoryReleaseDTO>>() {}.type
                    val jsonString = responseBody.use { it.string() }
                    val releaseVersions =
                        gson.fromJson<List<GitHubRepositoryReleaseDTO>>(jsonString, collectionType)
                            .map { it.toGithubReleaseVersion() }
                    Result.success(releaseVersions)
                }
            ).let {
                emit(it)
            }
        }.flowOn(Dispatchers.IO)

    private inline fun <T, R> handleApiResponse(
        call: () -> retrofit2.Response<T>,
        onSuccess: (T) -> Result<R>
    ): Result<R> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let { body -> onSuccess(body) }
                    ?: Result.failure(GithubRepoException(GitHubRepositoriesErrors.UNKNOWN_ERROR))
            } else {
                Result.failure(GithubRepoException(mapHttpErrorToGithubError(response.code())))
            }
        } catch (e: Exception) {
            Timber.e(e, "API call failed")
            Result.failure(GithubRepoException(GitHubRepositoriesErrors.SERVICE_UNAVAILABLE))
        }
    }
}