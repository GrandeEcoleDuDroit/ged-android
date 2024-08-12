package com.upsaclay.common.ui.components

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.upsaclay.common.R
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing

@Composable
fun ImageWithIcon(
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    imageUrl: String,
    contentDescription: String,
    iconVector: ImageVector,
    iconColor: Color = Color.White,
    iconBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    cacheKey: String? = null,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = modifier.size(100.dp * scale)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .diskCacheKey(cacheKey)
                .memoryCacheKey(cacheKey)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = onClick?.let {
                Modifier
                    .align(Alignment.Center)
                    .size(100.dp * scale)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
                    .clickable(onClick = it)
            } ?: run {
                Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
            }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = onClick?.let {
                Modifier
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .align(Alignment.BottomEnd)
                    .size(30.dp * scale)
                    .clickable(onClick = it)
            } ?: run {
                Modifier
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .align(Alignment.BottomEnd)
                    .size(30.dp * scale)
            }
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = "",
                tint = iconColor,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.extraSmall)
                    .size(16.dp * scale)
            )
        }
    }
}

@Composable
fun ImageWithIcon(
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    imageUri: Uri,
    contentDescription: String,
    iconVector: ImageVector,
    iconColor: Color = Color.White,
    iconBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    cacheKey: String? = null,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = modifier.size(100.dp * scale)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .diskCacheKey(cacheKey)
                .memoryCacheKey(cacheKey)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = onClick?.let {
                Modifier
                    .align(Alignment.Center)
                    .size(100.dp * scale)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
                    .clickable(onClick = it)
            } ?: run {
                Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
            }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = onClick?.let {
                Modifier
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .align(Alignment.BottomEnd)
                    .size(30.dp * scale)
                    .clickable(onClick = it)
            } ?: run {
                Modifier
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .align(Alignment.BottomEnd)
                    .size(30.dp * scale)
            }
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = "",
                tint = iconColor,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.extraSmall)
                    .size(16.dp * scale)
            )
        }
    }
}

@Composable
fun ImageWithIcon(
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    imageUrl: String,
    contentDescription: String,
    @DrawableRes iconRes: Int,
    iconColor: Color = Color.White,
    iconBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = modifier.size(100.dp * scale)) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = onClick?.let {
                Modifier
                    .align(Alignment.Center)
                    .size(100.dp * scale)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
                    .clickable(onClick = it)
            } ?: run {
                Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
            }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = onClick?.let {
                Modifier
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .align(Alignment.BottomEnd)
                    .size(30.dp * scale)
                    .clickable(onClick = it)
            } ?: run {
                Modifier
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .align(Alignment.BottomEnd)
                    .size(30.dp * scale)
            }
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = "",
                tint = iconColor,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.extraSmall)
                    .size(16.dp * scale)
            )
        }
    }
}

@Preview
@Composable
private fun ImageWithIconPreview() {
    GedoiseTheme {
        Box(modifier = Modifier.size(100.dp)) {
            Image(
                painter = painterResource(id = R.drawable.default_profile_picture),
                contentDescription = ""
            )
            ImageWithIcon(
                imageUrl = "",
                contentDescription = "",
                iconVector = Icons.Default.Edit
            )
        }
    }
}