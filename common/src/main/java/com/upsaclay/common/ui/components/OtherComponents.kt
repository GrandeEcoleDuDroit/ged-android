package com.upsaclay.common.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.upsaclay.common.R
import com.upsaclay.common.data.model.MenuItemData
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshComponent(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()
    Box(
        modifier = modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
    ) {
        if(pullToRefreshState.isRefreshing){
            LaunchedEffect(true) {
                onRefresh()
                delay(3000)
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter).zIndex(1000f)
        )
        content()
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    menuItemData: MenuItemData
) {
    Row(
        modifier = menuItemData.onClick?.let { onClick ->
            modifier
                .clickable(onClick = onClick)
                .padding(MaterialTheme.spacing.medium)
        } ?: run {
            modifier.padding(
                vertical = MaterialTheme.spacing.medium,
                horizontal = MaterialTheme.spacing.smallMedium
            )
        },
        verticalAlignment = Alignment.CenterVertically
    ) {

        menuItemData.icon?.let { icon ->
            icon()
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))
        }

        menuItemData.text()
    }
}

@Preview(showBackground = true, widthDp = 220)
@Composable
fun MenuItemPreview() {
    GedoiseTheme {
        MenuItem(
            modifier = Modifier.fillMaxWidth(),
            menuItemData = MenuItemData(
                text = { Text(text = "Item") },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = null
                    )
                }
            )
        )
    }
}