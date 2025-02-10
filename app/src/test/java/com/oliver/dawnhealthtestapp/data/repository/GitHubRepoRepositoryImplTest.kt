package com.oliver.dawnhealthtestapp.data.repository

import com.google.gson.Gson
import com.oliver.dawnhealthtestapp.data.remote.GithubRepositoriesApi
import com.oliver.dawnhealthtestapp.data.remote.dto.GitHubRepositoriesResponse
import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.domain.util.GitHubRepositoriesErrors
import com.oliver.dawnhealthtestapp.domain.util.GithubRepoException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class GitHubRepoRepositoryImplTest {

    private lateinit var repository: GitHubRepoRepositoryImpl
    private val api: GithubRepositoriesApi = mockk()
    private val gson = Gson()

    @BeforeEach
    fun setUp() {
        repository = GitHubRepoRepositoryImpl(api, gson)
    }

    @Test
    fun `searchRepositories should return success when API call is successful`() = runTest {
        val searchQuery = "Kotlin"
        val mockResponse = mockk<GitHubRepositoriesResponse>(relaxed = true)

        every { mockResponse.totalCount } returns 5
        every { mockResponse.items } returns emptyList()
        coEvery { api.searchRepositories(searchQuery) } returns Response.success(mockResponse)

        val result = repository.searchGitHubRepositories(searchQuery).first()

        assertTrue(result.isSuccess)
        assertEquals(5, result.getOrNull()?.first)
        assertEquals(emptyList<GithubRepository>(), result.getOrNull()?.second)

        coVerify { api.searchRepositories(searchQuery) }
    }

    @Test
    fun `searchRepositories should return error when API call fails`() = runTest {
        val searchQuery = "Kotlin"
        coEvery { api.searchRepositories(searchQuery) } returns Response.error(
            500,
            mockk<ResponseBody>(relaxed = true)
        )
        val result = repository.searchGitHubRepositories(searchQuery).first()

        assertTrue(result.isFailure)
        assertInstanceOf(GithubRepoException::class.java, result.exceptionOrNull())
        assertEquals(
            GitHubRepositoriesErrors.SERVER_ERROR,
            (result.exceptionOrNull() as GithubRepoException).error
        )
        coVerify { api.searchRepositories(searchQuery) }
    }

    @Test
    fun `getRepoReleaseVersion should return success when API call is successful`() = runTest {
        val owner = "JetBrains"
        val repo = "Kotlin"
        val mockJson = "[{\"tag_name\":\"1.8.0\",\"prerelease\":false}]"
        val mockResponseBody = mockk<ResponseBody>(relaxed = true)

        every { mockResponseBody.string() } returns mockJson
        coEvery { api.getRepositoryReleaseVersion(owner, repo) } returns Response.success(
            mockResponseBody
        )

        val result = repository.getRepositoryLastReleaseVersion(repo, owner).first()

        assertTrue(result.isSuccess)
        val releases = result.getOrNull()
        assertNotNull(releases)
        assertEquals(1, releases?.size)
        assertEquals("1.8.0", releases?.first()?.tag_name)

        coVerify { api.getRepositoryReleaseVersion(owner, repo) }
    }

    @Test
    fun `getRepoReleaseVersion should return error when API call fails`() = runTest {
        val owner = "JetBrains"
        val repo = "Kotlin"
        coEvery { api.getRepositoryReleaseVersion(owner, repo) } returns Response.error(
            404,
            mockk<ResponseBody>(relaxed = true)
        )
        val result = repository.getRepositoryLastReleaseVersion(repo, owner).first()

        assertTrue(result.isFailure)
        assertInstanceOf(GithubRepoException::class.java, result.exceptionOrNull())
        assertEquals(
            GitHubRepositoriesErrors.CLIENT_ERROR,
            (result.exceptionOrNull() as GithubRepoException).error
        )
        coVerify { api.getRepositoryReleaseVersion(owner, repo) }
    }
}