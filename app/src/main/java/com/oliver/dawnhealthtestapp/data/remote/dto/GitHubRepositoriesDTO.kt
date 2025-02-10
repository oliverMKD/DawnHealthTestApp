package com.oliver.dawnhealthtestapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepositoriesDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("full_name") val name: String,
    @SerializedName("name") val repoName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("owner") val owner: Owner,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("forks_count") val forksCount: Int,
    @SerializedName("language") val language: String?,
    @SerializedName("open_issues") val issueCount: Int
)

@Serializable
data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("login")
    val login: String,
)
