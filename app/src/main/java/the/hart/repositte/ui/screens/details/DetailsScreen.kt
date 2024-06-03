package the.hart.repositte.ui.screens.details

import  android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import the.hart.repositte.R
import the.hart.repositte.ui.screens.details.vm.DetailsScreenUiState
import the.hart.repositte.ui.views.ErrorItem
import the.hart.repositte.ui.views.IssueItem
import the.hart.repositte.ui.views.LoadingItem

private val previewUiState = DetailsScreenUiState(
    ownerAvatar = "https://th.bing.com/th/id/OIP.hSvCJ4IxH5iIxl6fUBZRlwHaHa?rs=1&pid=ImgDetMain",
    ownerName = LoremIpsum().values.joinToString().take(100),
    repoName = LoremIpsum().values.joinToString().take(100),
    repoDescription = LoremIpsum().values.joinToString().take(350),
    issues = null,
)

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,orientation=landscape,dpi=420",
)
@Composable
private fun DetailsScreenPreviewHorizontal() {
    DetailsScreen(
        navController = null,
        uiState = previewUiState,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun DetailsScreenPreviewVertical() {
    DetailsScreen(
        navController = null,
        uiState = previewUiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController?,
    uiState: DetailsScreenUiState,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 4.dp),
                title = {
                    Text(text = stringResource(id = R.string.details_title_toolbar))
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .clickable { navController?.navigateUp() }
                    )
                },
            )
        }
    ) { innerPadding ->

        val configuration = LocalConfiguration.current

        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                LayoutVertical(
                    innerPadding = innerPadding,
                    uiState = uiState,
                )
            }

            else -> {
                LayoutHorizontal(
                    innerPadding = innerPadding,
                    uiState = uiState,
                )
            }
        }
    }
}

@Composable
private fun LayoutVertical(
    innerPadding: PaddingValues,
    uiState: DetailsScreenUiState,
) {
    Column(
        modifier = Modifier.padding(
            top = innerPadding.calculateTopPadding() + 8.dp,
            bottom = innerPadding.calculateBottomPadding(),
            start = 8.dp,
            end = 8.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        DescriptionBlock(uiState = uiState)
        IssuesBlock(uiState = uiState)
    }
}

@Composable
private fun LayoutHorizontal(
    innerPadding: PaddingValues,
    uiState: DetailsScreenUiState,
) {
    Row(
        modifier = Modifier.run {
            padding(
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding(),
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + 8.dp,
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + 8.dp,
            )
        },
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DescriptionBlock(
            modifier = Modifier.weight(0.5f),
            uiState = uiState
        )
        IssuesBlock(
            modifier = Modifier.weight(0.5f),
            uiState = uiState,
        )
    }
}

@Composable
private fun DescriptionBlock(
    modifier: Modifier = Modifier,
    uiState: DetailsScreenUiState
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp)
    ) {
        if (uiState.isLoadingError) {
            Text(stringResource(id = R.string.details_loading_error))
        } else {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextBoxWithTitle(
                    title = stringResource(id = R.string.details_title_repo),
                    text = uiState.repoName
                )

                OwnerInfo(
                    avatarUrl = uiState.ownerAvatar,
                    name = uiState.ownerName
                )

                TextBoxWithTitle(
                    title = stringResource(id = R.string.details_title_desc),
                    text = uiState.repoDescription
                )
            }
        }
    }

}

@Composable
private fun IssuesBlock(
    modifier: Modifier = Modifier,
    uiState: DetailsScreenUiState
) {
    val lazyPagingItems = uiState.issues?.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        lazyPagingItems?.let {

            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { issueUiModel ->
                    IssueItem(
                        title = issueUiModel.title,
                        body = issueUiModel.body ?: ""
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

@Composable
private fun OwnerInfo(
    avatarUrl: String,
    name: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                model = avatarUrl,
                contentDescription = null,
            )
        }
        Column {
            Text(
                text = stringResource(id = R.string.details_title_owner),
                fontWeight = FontWeight.Bold
            )
            Text(text = name)
        }
    }
}

@Composable
private fun TextBoxWithTitle(
    title: String,
    text: String,
) {
    Column {
        Text(text = title, fontWeight = FontWeight.Bold)
        Text(text = text)
    }
}