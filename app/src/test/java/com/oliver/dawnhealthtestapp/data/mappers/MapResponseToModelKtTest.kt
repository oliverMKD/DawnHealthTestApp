package com.oliver.dawnhealthtestapp.data.mappers

import com.oliver.dawnhealthtestapp.data.remote.dto.GitHubRepositoriesDTO
import com.oliver.dawnhealthtestapp.data.remote.dto.GitHubRepositoryReleaseDTO
import com.oliver.dawnhealthtestapp.data.remote.dto.Owner
import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.domain.model.RepositoryReleaseVersion
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MapResponseToModelKtTest {

    @Test
    fun `toGithubRepositoryItem correctly maps GitHubRepositoriesDTO to GithubRepository`() {
        val dto = GitHubRepositoriesDTO(
            id = 123,
            name = "TestRepo",
            owner = Owner(login = "testOwner", avatarUrl = "testUrl"),
            language = null,
            description = null,
            stargazersCount = 100,
            forksCount = 50,
            issueCount = 10,
            repoName = "TestRepo"
        )
        val result = dto.toGithubRepositoryItem()

        val expected = GithubRepository(
            id = 123,
            name = "TestRepo",
            imageUrl = "testUrl",
            language = "Not Available",
            description = "Not Available",
            stargazersCount = 100,
            forksCount = 50,
            issuesCount = 10,
            repoOwner = "testOwner",
            repoName = "TestRepo"
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toGithubReleaseVersion correctly maps GitHubRepositoryReleaseDTO to RepositoryReleaseVersion`() {
        val dto = GitHubRepositoryReleaseDTO(
            tag_name = "v1.0.0",
            prerelease = false
        )
        val result = dto.toGithubReleaseVersion()

        val expected = RepositoryReleaseVersion(
            tag_name = "v1.0.0",
            prerelease = false
        )
        assertEquals(expected, result)
    }

}