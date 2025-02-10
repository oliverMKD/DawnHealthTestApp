package com.oliver.dawnhealthtestapp.presentation.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oliver.dawnhealthtestapp.R
import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.domain.util.GitHubRepositoriesErrors
import com.oliver.dawnhealthtestapp.presentation.components.ProgressIndicator
import com.oliver.dawnhealthtestapp.presentation.components.RepositoryCard
import com.oliver.dawnhealthtestapp.presentation.components.SearchBar
import com.oliver.dawnhealthtestapp.presentation.model.GitHubRepositoryEvent
import com.oliver.dawnhealthtestapp.presentation.navigation.HomeGraph
import com.oliver.dawnhealthtestapp.presentation.screens.destinations.GitHubRepositoryDetailsScreenDestination
import com.oliver.dawnhealthtestapp.presentation.viewmodel.GitHubRepositoriesViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@HomeGraph(start = true)
@Destination
@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: DestinationsNavigator,
    viewModel: GitHubRepositoriesViewModel
) {
    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = state.error) {
        val message = when (state.error) {
            GitHubRepositoriesErrors.SERVICE_UNAVAILABLE -> context.getString(R.string.error_service_unavailable)
            GitHubRepositoriesErrors.CLIENT_ERROR -> context.getString(R.string.client_error)
            GitHubRepositoriesErrors.SERVER_ERROR -> context.getString(R.string.server_error)
            GitHubRepositoriesErrors.UNKNOWN_ERROR -> context.getString(R.string.unknown_error)
            else -> null
        }
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onEvent(GitHubRepositoryEvent.OnErrorSeen)
        }
    }
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        SearchScreenContent(
            modifier = Modifier.padding(paddingValues),
            repositoriesList = state.list,
            numberOfResults = state.count,
            isSearching = state.isSearching,
            searchQuery = state.searchQuery,
            onEvent = viewModel::onEvent,
            onItemSelected = { repository ->
                navController.navigate(
                    GitHubRepositoryDetailsScreenDestination(repositoryItem = repository)
                )
            }
        )
    }
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier,
    repositoriesList: List<GithubRepository>,
    numberOfResults: Int,
    isSearching: Boolean,
    searchQuery: String,
    onEvent: (GitHubRepositoryEvent) -> Unit,
    onItemSelected: (GithubRepository) -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        SearchBar(
            input = searchQuery,
            hint = stringResource(R.string.search_for_repository),
            onSearch = {
                onEvent(GitHubRepositoryEvent.OnSearch(it))
            }
        )
        if (isSearching) {
            ProgressIndicator()
        } else {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                AnimatedVisibility(repositoriesList.isNotEmpty()) {
                    Text(
                        text = "$numberOfResults results",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
                RepositoriesList(
                    repositoriesList = repositoriesList,
                    onItemSelected = onItemSelected,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun RepositoriesList(
    modifier: Modifier = Modifier,
    repositoriesList: List<GithubRepository>,
    onItemSelected: (GithubRepository) -> Unit,
    onEvent: (GitHubRepositoryEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(repositoriesList.count()) { index ->
            val repository = repositoriesList[index]
            if (repository != null) {
                RepositoryCard(
                    image = repository.imageUrl,
                    name = repository.name,
                    description = repository.description,
                    onClick = {
                        onEvent(
                            GitHubRepositoryEvent.OnDetails(
                                repository.repoOwner,
                                repository.repoName
                            )
                        )
                        onItemSelected(repository)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    val sampleRepo = GithubRepository(
        id = 1,
        name = "JetBrains/Kotlin",
        imageUrl = "",
        description = "The Kotlin Programming Language",
        language = "Kotlin",
        forksCount = 2000,
        issuesCount = 500,
        stargazersCount = 10000,
        repoOwner = "JetBrains",
        repoName = "Kotlin"
    )

    MaterialTheme {
        SearchScreenContent(
            modifier = Modifier,
            repositoriesList = listOf(sampleRepo),
            numberOfResults = 1,
            isSearching = false,
            searchQuery = "Kotlin",
            onEvent = {},
            onItemSelected = {}
        )
    }
}

