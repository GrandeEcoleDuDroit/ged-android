package com.upsaclay.news.presentation.post.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.supportingText
import com.upsaclay.common.utils.ElapsedTimeValueFormat
import com.upsaclay.common.utils.getElapsedTimeValue
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.presentation.post.SizeTokens
import java.time.LocalDateTime

@Composable
fun PostSourceItem(
    modifier: Modifier = Modifier,
    postSource: Post.PostSource,
    date: LocalDateTime,
    contentSize: SizeTokens = SizeTokens.SMALL,
    elapsedTimeValueFormat: ElapsedTimeValueFormat = ElapsedTimeValueFormat.SHORT
) {
    val textStyle = when (contentSize) {
        SizeTokens.SMALL -> MaterialTheme.typography.bodySmall
        else -> MaterialTheme.typography.bodyMedium
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.smallSpacing()
    ) {
        PostSourceIcon(
            postSource = postSource,
            size = contentSize
        )

        Text(
            text = postSource.label,
            color = MaterialTheme.colorScheme.supportingText,
            style = textStyle
        )

        Text(
            text = "\u2022",
            color = MaterialTheme.colorScheme.supportingText,
            style = textStyle
        )

        Text(
            text = getElapsedTimeValue(date, elapsedTimeValueFormat),
            color = MaterialTheme.colorScheme.supportingText,
            style = textStyle
        )
    }
}

@Composable
private fun PostSourceIcon(
    postSource: Post.PostSource,
    size: SizeTokens
) {
    val iconSize = when (size) {
        SizeTokens.SMALL -> dimensionResource(com.upsaclay.common.R.dimen.small_icon_size)
        else -> dimensionResource(com.upsaclay.common.R.dimen.small_icon_size)
    }

    when (postSource) {
        Post.PostSource.LINKEDIN -> {
            Image(
                modifier = Modifier.size(iconSize),
                painter = painterResource(R.drawable.ic_linkedin),
                contentDescription = null
            )
        }

        Post.PostSource.INSTAGRAM -> {
            Image(
                modifier = Modifier.size(iconSize),
                painter = painterResource(R.drawable.ic_instagram),
                contentDescription = null
            )
        }

        Post.PostSource.BLOG_LLM -> {
            Image(
                modifier = Modifier.size(iconSize),
                painter = painterResource(com.upsaclay.common.R.drawable.ged_logo),
                contentDescription = null
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PostSourceItemPreview() {
    GedoiseTheme {
        Surface {
            PostSourceItem(
                postSource = Post.PostSource.BLOG_LLM,
                date = LocalDateTime.now()
            )
        }
    }
}