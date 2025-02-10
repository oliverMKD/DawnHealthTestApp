package com.oliver.dawnhealthtestapp.presentation.viewmodel

import app.cash.turbine.test
import com.oliver.dawnhealthtestapp.CoroutineProvider
import com.oliver.dawnhealthtestapp.CoroutineTest
import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.domain.model.RepositoryReleaseVersion
import com.oliver.dawnhealthtestapp.domain.repository.GitHubRepoRepository
import com.oliver.dawnhealthtestapp.domain.util.GitHubRepositoriesErrors
import com.oliver.dawnhealthtestapp.domain.util.GithubRepoException
import com.oliver.dawnhealthtestapp.presentation.model.GitHubRepositoryEvent
import com.oliver.dawnhealthtestapp.presentation.model.HomeUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GitHubRepositoriesViewModelTest : CoroutineTest {

    override lateinit var testCoroutineProvider: CoroutineProvider
    override lateinit var testScope: TestScope
    private lateinit var viewModel: GitHubRepositoriesViewModel
    private val repository: GitHubRepoRepository = mockk()

    @BeforeEach
    fun setUp() {
        viewModel = GitHubRepositoriesViewModel(
            repository
        )
    }

    private val gitHubRepository = GithubRepository(
        id = 123456,
        name = "JetBrains",
        imageUrl = "desc",
        description = "",
        language = "",
        forksCount = 0,
        issuesCount = 0,
        stargazersCount = 0,
        repoName = "",
        repoOwner = ""
    )

    private val repositories = listOf(gitHubRepository)

    @Test
    fun `onEvent OnSearch should update state and trigger search`() = runTest {
        val query = "Kotlin"
        coEvery { repository.searchGitHubRepositories(query) } returns flowOf(Result.success(10 to repositories))

        viewModel.stateFlow.test {
            viewModel.onEvent(GitHubRepositoryEvent.OnSearch(query))
            skipItems(1)
            assertEquals(
                HomeUiState(
                    searchQuery = query,
                    isSearching = true
                ), awaitItem()
            )
            assertEquals(
                HomeUiState(
                    searchQuery = query,
                    list = repositories,
                    count = 10,
                    isSearching = false
                ), awaitItem()
            )
            coVerify { repository.searchGitHubRepositories(query) }
        }
    }

    @Test
    fun `onEvent OnSearch should handle empty query`() = runTest {
        viewModel.stateFlow.test {
            viewModel.onEvent(GitHubRepositoryEvent.OnSearch(""))
            skipItems(1)
            assertEquals(
                HomeUiState(
                    searchQuery = "",
                    isSearching = true
                ),
                awaitItem()
            )
            assertEquals(
                HomeUiState(
                    list = emptyList(),
                    count = 0,
                    searchQuery = "",
                    isSearching = false,
                    error = null,
                    releaseDate = ""
                ),
                awaitItem()
            )
        }
        coVerify(exactly = 0) { repository.searchGitHubRepositories(any()) }
    }

    @Test
    fun `onEvent OnSearch should handle search failure`() = runTest {
        val query = "Kotlin"
        coEvery { repository.searchGitHubRepositories(query) } returns flowOf(
            Result.failure(
                GithubRepoException(GitHubRepositoriesErrors.SERVICE_UNAVAILABLE)
            )
        )
        viewModel.stateFlow.test {
            viewModel.onEvent(GitHubRepositoryEvent.OnSearch(query))
            skipItems(1)

            assertEquals(
                HomeUiState(
                    searchQuery = query,
                    isSearching = true
                ), awaitItem()
            )

            assertEquals(
                HomeUiState(
                    searchQuery = query,
                    isSearching = false,
                    error = GitHubRepositoriesErrors.SERVICE_UNAVAILABLE
                ), awaitItem()
            )
        }
        coVerify { repository.searchGitHubRepositories(query) }
    }

    @Test
    fun `onEvent OnDetails should fetch latest release`() = runTest {
        val owner = "JetBrains"
        val repo = "Kotlin"
        val release = RepositoryReleaseVersion(
            tag_name = "1.8.0",
            prerelease = false
        )
        coEvery { repository.getRepositoryLastReleaseVersion(repo, owner) } returns flowOf(
            Result.success(
                listOf(release)
            )
        )

        viewModel.stateFlow.test {
            viewModel.onEvent(GitHubRepositoryEvent.OnDetails(owner, repo))
            skipItems(1)
            assertEquals(HomeUiState(isSearching = true), awaitItem())
            assertEquals(
                HomeUiState(releaseDate = release.tag_name, isSearching = false),
                awaitItem()
            )
        }

        coVerify { repository.getRepositoryLastReleaseVersion(repo, owner) }
    }

    @Test
    fun `onEvent OnDetails should handle API failure`() = runTest {
        val owner = "JetBrains"
        val repo = "Kotlin"
        coEvery { repository.getRepositoryLastReleaseVersion(repo, owner) } returns
                flowOf(Result.failure(GithubRepoException(GitHubRepositoriesErrors.UNKNOWN_ERROR)))

        viewModel.stateFlow.test {
            viewModel.onEvent(GitHubRepositoryEvent.OnDetails(owner, repo))
            skipItems(1)
            assertEquals(HomeUiState(isSearching = true), awaitItem())
            assertEquals(
                HomeUiState(
                    isSearching = false,
                    error = GitHubRepositoriesErrors.UNKNOWN_ERROR
                ), awaitItem()
            )
        }
        coVerify { repository.getRepositoryLastReleaseVersion(repo, owner) }
    }

    @Test
    fun `onEvent OnErrorSeen should reset error state`() = runTest {
        viewModel.onEvent(GitHubRepositoryEvent.OnErrorSeen)

        viewModel.stateFlow.test {
            assertEquals(HomeUiState(error = null), awaitItem())
        }
    }
}