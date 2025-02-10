package com.oliver.dawnhealthtestapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepositoriesResponse(
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<GitHubRepositoriesDTO>,
    @SerializedName("total_count") val totalCount: Int
)
