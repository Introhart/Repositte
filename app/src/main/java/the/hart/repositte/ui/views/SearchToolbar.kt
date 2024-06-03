package the.hart.repositte.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import the.hart.repositte.R

/**
 * Toolbar with expandable search view
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchToolbar(
    title: String,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
) {
    var isSearchExpanded by remember { mutableStateOf(false) }
    val focusRequester = FocusRequester()

    TopAppBar(
        modifier = Modifier
            .shadow(elevation = 4.dp)
            .background(MaterialTheme.colorScheme.primary),
        title = {
            if (!isSearchExpanded) {
                Text(title)
            }
        },
        navigationIcon = {
            if (isSearchExpanded) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .clickable {
                            isSearchExpanded = false
                            onClear()
                        }
                )
            }
        },
        actions = {
            if (!isSearchExpanded) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(id = R.string.search_title),
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(radius = 30.dp)
                        ) {
                            isSearchExpanded = true
                        }

                )
            } else {
                Box(modifier = Modifier.padding(horizontal = 6.dp)) {
                    DockedSearchBar(
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = onSearch,
                        active = true,
                        onActiveChange = {},
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.search_toolbar_text_hint),
                                color = Color.Gray.copy(alpha = 0.3f)
                            )
                        },
                        modifier = Modifier
                            .height(54.dp)
                            .focusRequester(focusRequester),
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .clickable { onClear() }
                                )
                            }
                        },
                        colors = SearchBarDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        content = {}
                    )

                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                }
            }
        },
    )
}