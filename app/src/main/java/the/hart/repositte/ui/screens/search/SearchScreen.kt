package the.hart.repositte.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import the.hart.repositte.R
import the.hart.repositte.ui.screens.search.vm.RepoPreviewUiState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import the.hart.repositte.domain.model.RepositoryDomain
import the.hart.repositte.ui.views.BottomListFader
import the.hart.repositte.ui.views.ErrorItem
import the.hart.repositte.ui.views.LoadingItem
import the.hart.repositte.ui.views.SearchToolbar

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        uiState = RepoPreviewUiState(),
        onQueryChange = {},
        search = {},
        clearQuery = {},
        navController = null,
    )
}

@Composable
fun SearchScreen(
    uiState: RepoPreviewUiState,
    onQueryChange: (String) -> Unit,
    search: () -> Unit,
    clearQuery: () -> Unit,
    navController: NavController?,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            SearchToolbar(
                title = stringResource(id = R.string.app_name),
                query = uiState.query,
                onClear = { clearQuery() },
                onSearch = {
                    search()
                    focusManager.clearFocus()
                },
                onQueryChange = { onQueryChange(it) }
            )
        }
    ) { innerPadding ->
        BackgroundIcon()
        RepoList(
            pagingData = uiState.pager,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 8.dp,
                end = 8.dp,
            ),
            navController = navController,
        )
        BottomListFader(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}

/**
 * List of repositories
 */
@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    pagingData: Flow<PagingData<RepositoryDomain>>? = null,
    navController: NavController?,
) {
    val lazyPagingItems = pagingData?.collectAsLazyPagingItems()
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
    ) {

        lazyPagingItems?.let {

            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { repoUiModel ->
                    SearchItem(
                        previewModel = repoUiModel,
                        modifier = Modifier.clickable {
                            navController?.navigate(
                                "details_screen/${repoUiModel.authorName}/${repoUiModel.repoName}"
                            )
                        }
                    )
                }
            }

            it.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = lazyPagingItems.loadState.refresh as LoadState.Error
                        item {
                            ErrorItem(message = e.error.localizedMessage ?: stringResource(id = R.string.error_unknown)) {
                                lazyPagingItems.retry()
                            }
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = lazyPagingItems.loadState.append as LoadState.Error
                        item {
                            ErrorItem(message = e.error.localizedMessage ?: stringResource(id = R.string.error_unknown)) {
                                lazyPagingItems.retry()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchItemPreview() {
    val itemModel = RepositoryDomain(
        authorAvatar = "https://pngimg.com/uploads/github/github_PNG40.png",
        authorName = "Author Name",
        repoName = "Repository Name",
        issueCount = 30,
        repoDescription = LoremIpsum().values.joinToString().take(200),
    )
    SearchItem(previewModel = itemModel)
}

@Composable
fun SearchItem(
    modifier: Modifier = Modifier,
    previewModel: RepositoryDomain,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp)

    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = previewModel.authorAvatar,
                        error = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                }

                Column {
                    Text(
                        text = stringResource(id = R.string.search_title_owner),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1
                    )
                    Divider(
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        thickness = 3.dp,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        text = previewModel.authorName,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            if (!previewModel.repoDescription.isNullOrEmpty()) {
                Column {
                    Text(
                        text = stringResource(id = R.string.search_title_desc),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Text(
                        text = previewModel.repoDescription ?: "",
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
private fun BackgroundIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier= Modifier.size(256.dp),
            painter = painterResource(id = R.drawable.ic_broken_heart),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
        )
    }
}