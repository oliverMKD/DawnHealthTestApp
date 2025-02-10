package com.oliver.dawnhealthtestapp.data.mappers

import com.oliver.dawnhealthtestapp.domain.util.GitHubRepositoriesErrors
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class MapHttpErrorKtTest {

    @ParameterizedTest
    @CsvSource(
        "500, SERVER_ERROR",
        "503, SERVER_ERROR",
        "404, CLIENT_ERROR",
        "401, CLIENT_ERROR",
        "399, UNKNOWN_ERROR",
        "600, UNKNOWN_ERROR"
    )
    fun `mapHttpErrorToGithubError returns correct error`(
        code: Int,
        expectedError: GitHubRepositoriesErrors
    ) {
        val result = mapHttpErrorToGithubError(code)
        assertEquals(expectedError, result)
    }
}