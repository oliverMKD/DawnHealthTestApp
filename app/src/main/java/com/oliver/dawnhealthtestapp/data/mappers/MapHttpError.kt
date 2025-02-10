package com.oliver.dawnhealthtestapp.data.mappers

import com.oliver.dawnhealthtestapp.domain.util.GitHubRepositoriesErrors

fun mapHttpErrorToGithubError(code: Int): GitHubRepositoriesErrors {
    return when (code) {
        in 500..599 -> GitHubRepositoriesErrors.SERVER_ERROR
        in 400..499 -> GitHubRepositoriesErrors.CLIENT_ERROR
        else -> GitHubRepositoriesErrors.UNKNOWN_ERROR
    }
}