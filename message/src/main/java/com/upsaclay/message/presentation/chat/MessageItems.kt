package com.upsaclay.message.presentation.chat

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.black
import com.upsaclay.common.presentation.theme.informationText
import com.upsaclay.common.presentation.theme.inputBackground
import com.upsaclay.common.presentation.theme.white
import com.upsaclay.common.utils.DateUtils
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.fixtures.messageFixture
import java.time.LocalDateTime

@Composable
fun SentMessageItem(
    modifier: Modifier = Modifier,
    message: Message,
    showSeen: Boolean = false,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val dateTimeTextColor = if (isSystemInDarkTheme()) Color.LightGray else Color(0xFFC8C8C8)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Spacer(modifier = Modifier.weight(0.2f))

        Column(
            modifier = Modifier.weight(0.8f, fill = false),
            horizontalAlignment = Alignment.End
        ) {
            MessageBubble(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onClick() },
                            onLongPress = { onLongClick() }
                        )
                    },
                text = message.content,
                textColor = Color.White,
                date = message.date,
                backgroundColor = MaterialTheme.colorScheme.primary,
                dateTimeTextColor = dateTimeTextColor,
            )

            if (showSeen) {
                Text(
                    modifier = Modifier.padding(
                        top = dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding),
                        end = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)
                    ),
                    text = stringResource(id = R.string.message_seen),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSystemInDarkTheme()) Color.Gray else Color.DarkGray
                )
            }
        }

        when (message.state) {
            MessageState.SENDING -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp).weight(0.1f)
                )
            }

            MessageState.ERROR -> {
                Icon(
                    painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_error),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp).weight(0.1f)
                )
            }

            else -> Unit
        }
    }
}

@Composable
fun ReceivedMessageItem(
    modifier: Modifier = Modifier,
    profilePictureUrl: String?,
    message: Message,
    displayProfilePicture: Boolean,
    onLongClick: () -> Unit,
    onInterlocutorClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val foreground = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.white else MaterialTheme.colorScheme.black

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.small_padding)),
        verticalAlignment = Alignment.Bottom
    ) {
        if (displayProfilePicture) {
            ProfilePicture(
                url = profilePictureUrl,
                scale = 0.3f,
                onClick = onInterlocutorClick
            )
        } else {
            ProfilePicture(
                modifier = Modifier.alpha(0f),
                url = null,
                scale = 0.3f
            )
        }

        MessageBubble(
            modifier = Modifier
                .weight(0.8f, fill = false)
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = { onLongClick() })
                },
            text = message.content,
            date = message.date,
            backgroundColor = MaterialTheme.colorScheme.inputBackground,
            textColor = foreground,
            dateTimeTextColor = Color(0xFF8E8E93)
        )

        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MessageBubble(
    modifier: Modifier = Modifier,
    text: String,
    date: LocalDateTime,
    textColor: Color,
    dateTimeTextColor: Color,
    backgroundColor: Color
) {
    FlowRow(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))
            .background(backgroundColor)
            .padding(
                vertical = dimensionResource(com.upsaclay.common.R.dimen.small_padding),
                horizontal = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)
            ),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier.testTag(stringResource(R.string.chat_screen_message_text_tag)),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )

        Text(
            modifier = Modifier
                .padding(start = dimensionResource(com.upsaclay.common.R.dimen.small_padding))
                .align(Alignment.Bottom),
            text = DateUtils.formatHourMinute(date),
            style = MaterialTheme.typography.labelSmall,
            color = dateTimeTextColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clip(ShapeDefaults.ExtraLarge)
            .background(MaterialTheme.colorScheme.inputBackground)
            .padding(end = dimensionResource(com.upsaclay.common.R.dimen.small_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onSurface),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            cursorBrush = SolidColor(TextFieldDefaults.colors().cursorColor),
            maxLines = 6
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.message_placeholder),
                        style = TextStyle(platformStyle = PlatformTextStyle(false)),
                    )
                },
                enabled = true,
                singleLine = false,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.inputBackground,
                    unfocusedContainerColor = MaterialTheme.colorScheme.inputBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = TextFieldDefaults.colors().cursorColor
                ),
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                    vertical = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)
                )
            )
        }

        if (value.isNotBlank()) {
            Button(
                modifier = Modifier
                    .testTag(stringResource(R.string.chat_screen_send_button_tag)),
                onClick = onSendClick,
                contentPadding = PaddingValues()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = stringResource(id = R.string.send_message_icon_description),
                    tint = MaterialTheme.colorScheme.white
                )
            }
        }
    }
}

@Composable
fun NewMessageIndicator(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)),
        Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.clickable { onClick() },
            shadowElevation = 2.dp,
            shape = ShapeDefaults.Small,
            color = Color.White
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(com.upsaclay.common.R.dimen.large_padding),
                        vertical = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)
                    ),
                text = stringResource(id = R.string.new_message),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.black
            )
        }
    }
}

@Composable
fun MessageBlockedUserIndicator(
    modifier: Modifier = Modifier,
    onDeleteChatClick: () -> Unit,
    onUnblockUserClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(com.upsaclay.common.R.string.blocked_user),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.chat_blocked_user_indicator_text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.informationText,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton (
                onClick = onDeleteChatClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0f),
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(com.upsaclay.common.R.string.delete),
                    color = MaterialTheme.colorScheme.error
                )
            }

            TextButton(
                onClick = onUnblockUserClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(com.upsaclay.common.R.string.unblock)
                )
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun SeenSentMessageItemPreview() {
    GedoiseTheme {
        SentMessageItem(
            message = messageFixture.copy(content = "Hahaha"),
            showSeen = true,
            onClick = {},
            onLongClick = {}
        )
    }
}

@Preview
@Composable
private fun SendingSentMessageItemPreview() {
    GedoiseTheme {
        SentMessageItem(
            message = messageFixture.copy(state = MessageState.SENDING),
            onClick = {},
            onLongClick = {}
        )
    }
}

@Preview
@Composable
private fun ErrorSentMessageItemPreview() {
    GedoiseTheme {
        SentMessageItem(
            message = messageFixture.copy(state = MessageState.ERROR),
            onClick = {},
            onLongClick = {}
        )
    }
}

@Preview(showBackground = false)
@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReceiveMessageItemPreview() {
    GedoiseTheme {
        ReceivedMessageItem(
            message = messageFixture,
            displayProfilePicture = true,
            profilePictureUrl = "",
            onLongClick = {},
            onInterlocutorClick = {}
        )
    }
}

@Preview(showBackground = false)
@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MessageTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    GedoiseTheme {
        MessageInput(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            onSendClick = {},
        )
    }
}

@Preview
@Composable
private fun NewMessageIndicatorPreview() {
    GedoiseTheme {
        NewMessageIndicator {}
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MessageBlockedUserIndicatorPreview() {
    GedoiseTheme {
        Surface {
            MessageBlockedUserIndicator(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                onDeleteChatClick = {},
                onUnblockUserClick = {}
            )
        }
    }
}