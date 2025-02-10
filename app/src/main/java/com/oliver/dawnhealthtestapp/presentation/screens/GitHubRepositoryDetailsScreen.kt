package com.oliver.dawnhealthtestapp.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.oliver.dawnhealthtestapp.R
import com.oliver.dawnhealthtestapp.domain.model.GithubRepository
import com.oliver.dawnhealthtestapp.presentation.components.ItemValueCard
import com.oliver.dawnhealthtestapp.presentation.components.ProgressIndicator
import com.oliver.dawnhealthtestapp.presentation.navigation.HomeGraph
import com.oliver.dawnhealthtestapp.presentation.viewmodel.GitHubRepositoriesViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@HomeGraph
@Destination
@Composable
fun GitHubRepositoryDetailsScreen(
    modifier: Modifier = Modifier,
    repositoryItem: GithubRepository,
    navController: DestinationsNavigator,
    viewModel: GitHubRepositoriesViewModel,
) {

    val state by viewModel.stateFlow.collectAsState()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        RepositoryDetails(
            modifier = Modifier.padding(paddingValues),
            title = repositoryItem.name,
            image = repositoryItem.imageUrl,
            language = repositoryItem.language,
            numberOfForks = repositoryItem.forksCount,
            openIssues = repositoryItem.issuesCount,
            stars = repositoryItem.stargazersCount,
            releaseVersion = state.releaseDate,
            isSearching = state.isSearching,
            onBackClicked = navController::popBackStack
        )
    }
}

@Composable
fun RepositoryDetails(
    modifier: Modifier,
    title: String,
    image: String,
    language: String,
    numberOfForks: Int,
    openIssues: Int,
    stars: Int,
    releaseVersion: String,
    isSearching: Boolean,
    onBackClicked: () -> Unit
) {
    if (isSearching) {
        ProgressIndicator()
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            item {
                RepositoryImage(
                    image = image,
                    name = title,
                    language = language,
                    onBackClicked = onBackClicked
                )
            }

            item {
                RepositoryInformation(
                    forks = numberOfForks,
                    openIssues = openIssues,
                    stars = stars,
                    releaseVersion = releaseVersion
                )
            }
        }
    }

}

@Composable
fun RepositoryImage(
    modifier: Modifier = Modifier,
    image: String?,
    name: String,
    language: String,
    onBackClicked: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .align(Alignment.TopStart)
                .clickable(onClick = onBackClicked),
            contentDescription = "Back icon",
            painter = painterResource(R.drawable.arrow_back),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            image?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    error = painterResource(R.drawable.ic_launcher_background),
                    modifier = Modifier.requiredSize(100.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
            )
            Text(
                text = language,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun RepositoryInformation(
    forks: Int,
    openIssues: Int,
    stars: Int,
    releaseVersion: String
) {
    ItemValueCard("Forks", forks.toString())
    ItemValueCard("Issues", openIssues.toString())
    ItemValueCard("Stars", stars.toString())
    ItemValueCard("Last Release date", releaseVersion)
}

@Preview(showBackground = true)
@Composable
fun RepositoryDetailsPreview() {
    MaterialTheme {
        RepositoryDetails(
            modifier = Modifier,
            title = "JetBrains/Kotlin",
            image = "",
            language = "Kotlin",
            numberOfForks = 2000,
            openIssues = 500,
            stars = 10000,
            releaseVersion = "v1.9.0",
            isSearching = false,
            onBackClicked = {}
        )
    }
}


