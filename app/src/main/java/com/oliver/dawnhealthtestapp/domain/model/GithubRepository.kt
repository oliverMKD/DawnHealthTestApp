package com.oliver.dawnhealthtestapp.domain.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class GithubRepository(
    val id: Long,
    val name: String,
    val imageUrl: String = "",
    val description: String,
    val language: String,
    val forksCount: Int,
    val issuesCount: Int,
    val stargazersCount: Int,
    val repoName: String,
    val repoOwner : String
)
