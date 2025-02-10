package com.oliver.dawnhealthtestapp.domain.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class RepositoryReleaseVersion(
    val tag_name : String,
    val prerelease : Boolean
)
