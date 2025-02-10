package com.oliver.dawnhealthtestapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepositoryReleaseDTO(
    @SerializedName("tag_name") val tag_name : String,
    @SerializedName("prerelease") val prerelease : Boolean
)

