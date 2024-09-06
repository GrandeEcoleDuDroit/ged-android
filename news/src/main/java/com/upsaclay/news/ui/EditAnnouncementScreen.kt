package com.upsaclay.news.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.R
import com.upsaclay.news.announcementFixture
import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.model.AnnouncementState
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@Composable
fun EditAnnouncementScreen(
    navController: NavController,
    newsViewModel: NewsViewModel = koinViewModel(),
    isModification: Boolean = false
) {
    var title: String? by remember { mutableStateOf(null) }
    var content by remember { mutableStateOf("") }
    val user = newsViewModel.user.collectAsState(null).value
    val state = newsViewModel.announcementState.collectAsState(AnnouncementState.DEFAULT).value
    val context = LocalContext.current
    val currentAnnouncement = remember { newsViewModel.displayedAnnouncement }
    LaunchedEffect(Unit) {
        if(isModification) {
            title = currentAnnouncement!!.title
            content = currentAnnouncement.content
        }
    }

    LaunchedEffect(state) {
        when(state) {
            AnnouncementState.ANNOUNCEMENT_CREATED -> navController.popBackStack()
            AnnouncementState.ERROR_ANNOUNCEMENT_CREATION -> Toast.makeText(
                context,
                "Error to create announcement",
                Toast.LENGTH_SHORT
            ).show()
            else -> {}
        }
    }

    user?.let {
        Scaffold(
            topBar = {
                if(isModification) {
                    EditAnnouncementTopBar(
                        navController = navController,
                        newsViewModel = newsViewModel,
                        isButtonEnable = content.isNotEmpty(),
                        isModification = true,
                        announcement = currentAnnouncement!!.copy(
                            title = title,
                            content = content,
                            date = LocalDateTime.now()
                        )
                    )
                }
                else {
                    EditAnnouncementTopBar(
                        navController = navController,
                        newsViewModel = newsViewModel,
                        isButtonEnable = content.isNotEmpty(),
                        isModification = false,
                        announcement = Announcement(
                            id = -1,
                            title = title,
                            content = content,
                            date = LocalDateTime.now(),
                            author = user
                        )
                    )
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(
                        top = contentPadding.calculateTopPadding(),
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    )
                    .fillMaxSize()
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title_field),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = title ?: "",
                    onValueChange = { title = it },
                    textStyle = MaterialTheme.typography.titleLarge
                        .plus(TextStyle(fontWeight = FontWeight.Bold)),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )

                TextField(
                    modifier = Modifier.fillMaxSize(),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.content_field),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = content,
                    onValueChange = { content = it },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditAnnouncementTopBar(
    navController: NavController,
    newsViewModel: NewsViewModel,
    isButtonEnable: Boolean = false,
    isModification: Boolean,
    announcement: Announcement
) {
    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack()}) {
                Icon(
                    modifier = Modifier.size(MaterialTheme.spacing.extraLarge),
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = com.upsaclay.common.R.string.close)
                )
            }
        },
        actions = {
            if (isModification) {
                Button(
                    enabled = isButtonEnable,
                    onClick = { newsViewModel.updateAnnouncement(announcement) }
                ) {
                    Text(text = stringResource(id = com.upsaclay.common.R.string.save))
                }
            } else {
                Button(
                    enabled = isButtonEnable,
                    onClick = { newsViewModel.createAnnouncement(announcement) }
                ) {
                    Text(text = stringResource(id = com.upsaclay.common.R.string.publish))
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EditAnnouncementScreenPreview() {
    var title by remember { mutableStateOf(announcementFixture.title) }
    var content by remember { mutableStateOf(announcementFixture.content) }

    GedoiseTheme {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                modifier = Modifier.size(MaterialTheme.spacing.extraLarge),
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = com.upsaclay.common.R.string.close)
                            )
                        }
                    },
                    actions = {
                        Button(
                            enabled = true,
                            onClick = {}
                        ) {
                            Text(text = stringResource(id = com.upsaclay.common.R.string.save))
                        }
                    }
                )
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(
                        top = contentPadding.calculateTopPadding(),
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    )
                    .fillMaxSize()
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title_field),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = title ?: "",
                    onValueChange = { title = it },
                    textStyle = MaterialTheme.typography.titleLarge
                        .plus(TextStyle(fontWeight = FontWeight.Bold)),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )

                TextField(
                    modifier = Modifier.fillMaxSize(),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.content_field),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = content,
                    onValueChange = { content = it },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CreateAnnouncementScreenPreview() {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    GedoiseTheme {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "")
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                modifier = Modifier.size(MaterialTheme.spacing.extraLarge),
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = com.upsaclay.common.R.string.close)
                            )
                        }
                    },
                    actions = {
                        Button(
                            contentPadding = PaddingValues(
                               horizontal = MaterialTheme.spacing.medium,
                            ),
                            onClick = { }
                        ) {
                            Text(text = stringResource(id = com.upsaclay.common.R.string.publish))
                        }
                    }
                )
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(
                        top = contentPadding.calculateTopPadding(),
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    )
                    .fillMaxSize()
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title_field),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = title,
                    onValueChange = { title = it },
                    textStyle = MaterialTheme.typography.titleLarge
                        .plus(TextStyle(fontWeight = FontWeight.Bold)),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )

                TextField(
                    modifier = Modifier.fillMaxSize(),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.content_field),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = content,
                    onValueChange = { content = it },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
            }
        }
    }
}