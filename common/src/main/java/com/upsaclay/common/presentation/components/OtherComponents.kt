package com.upsaclay.common.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.displayName
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshComponent(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    refreshing: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(refreshing) {
            if (!refreshing) {
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1000f)
        )
        content()
    }
}

@Composable
fun TextItem(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        headlineContent = text,
        leadingContent = icon
    )
}

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: User,
    imageScale: Float = 0.5f,
    textStyle: TextStyle = LocalTextStyle.current,
    trailingContent: @Composable (() -> Unit)? = null
) {
    ListItem(
        modifier = modifier.fillMaxWidth(),
        leadingContent = {
            ProfilePicture(
                url = user.profilePictureUrl,
                scale = imageScale
            )
        },
        headlineContent = { Text(text = user.displayName(), style = textStyle) },
        trailingContent = trailingContent
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun ClickableMenuItemPreview() {
    GedoiseTheme {
        Surface {
            Column {
                TextItem(
                    modifier = Modifier.width(300.dp),
                    text = { Text(text = "Item") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_fill_person),
                            contentDescription = null
                        )
                    },
                    onClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 200, heightDp = 200)
@Composable
private fun PullRefreshComponentPreview() {
    var refreshing by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000)
        refreshing = false
    }

    GedoiseTheme {
        PullToRefreshComponent(
            onRefresh = { },
            refreshing = refreshing
        ) {
            LazyColumn(modifier = Modifier.padding(dimensionResource(R.dimen.medium_padding))) {
                item {
                    Text(text = "Pull to refresh", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

@PhonePreviews
@Composable
private fun UserItemPreview() {
    GedoiseTheme {
        Surface {
            UserItem(user = userFixture)
        }
    }
}