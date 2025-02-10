package com.oliver.dawnhealthtestapp.data.mappers

import com.oliver.dawnhealthtestapp.data.remote.dto.GitHubRepositoriesDTO
import com.oliver.dawnhealthtestapp.data.remote.dto.GitHubRepositoryReleaseDTO
import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.domain.model.RepositoryReleaseVersion

fun GitHubRepositoriesDTO.toGithubRepositoryItem(): GithubRepository {
    return GithubRepository(
        id = id,
        name = name,
        imageUrl = owner.avatarUrl,
        language = language ?: "Not Available",
        description = description ?: "Not Available",
        stargazersCount = stargazersCount,
        forksCount = forksCount,
        issuesCount = issueCount,
        repoOwner = owner.login,
        repoName = repoName
    )
}

fun GitHubRepositoryReleaseDTO.toGithubReleaseVersion(): RepositoryReleaseVersion {
    return RepositoryReleaseVersion(
        tag_name = tag_name,
        prerelease = prerelease
    )
}