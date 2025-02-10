package com.oliver.dawnhealthtestapp.domain.util

enum class GitHubRepositoriesErrors {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}

class GithubRepoException(val error: GitHubRepositoriesErrors) : Exception(
    "An error occurred when translating: $error"
)