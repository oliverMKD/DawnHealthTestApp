package com.oliver.dawnhealthtestapp.data.remote

import com.oliver.dawnhealthtestapp.data.remote.AppConfig.FIRST_PAGE
import com.oliver.dawnhealthtestapp.data.remote.AppConfig.PAGE_SIZE
import com.oliver.dawnhealthtestapp.data.remote.dto.GitHubRepositoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubRepositoriesApi {

    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int = FIRST_PAGE,
        @Query("per_page") perPage: Int = PAGE_SIZE
    ): Response<GitHubRepositoriesResponse>

}