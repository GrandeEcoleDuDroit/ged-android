package com.upsaclay.authentication.presentation.registration.thirdregistration

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import com.upsaclay.authentication.R
import com.upsaclay.authentication.presentation.components.OutlinePasswordTextField
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.checkBoxColor
import com.upsaclay.common.presentation.theme.padding
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun ThirdRegistrationForm(
    email: String,
    password: String,
    loading: Boolean,
    legalNoticeChecked: Boolean,
    @StringRes emailError: Int?,
    @StringRes passwordError: Int?,
    @StringRes errorMessage: Int?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLegalNoticeCheckedChange: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            Text(
                text = stringResource(id = R.string.enter_email_password),
                style = MaterialTheme.typography.titleMedium
            )

            SimpleOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(stringResource(R.string.registration_screen_email_input_tag)),
                value = email,
                enabled = !loading,
                onValueChange = onEmailChange,
                label = stringResource(com.upsaclay.common.R.string.email),
                errorMessage = emailError?.let { stringResource(it) }
            )

            OutlinePasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(stringResource(R.string.registration_screen_password_input_tag)),
                text = password,
                enabled = !loading,
                onValueChange = onPasswordChange,
                errorMessage = passwordError
            )
        }

        Row(
            modifier = Modifier.padding(horizontal = MaterialTheme.padding.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = legalNoticeChecked,
                onCheckedChange = onLegalNoticeCheckedChange,
                colors = MaterialTheme.colorScheme.checkBoxColor,
            )

            LegalNoticeText()
        }

        errorMessage?.let {
            Text(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.padding.medium)
                    .align(Alignment.Start),
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun LegalNoticeText() {
    val legalNoticeUrl = "https://itscloudymourchidi.github.io/itscloudy-website/#/legal-notice"

    Text(
        text = buildAnnotatedString {
            append(stringResource(R.string.agree_terms_privacy_beginning_text))
            append(" ")
            withLink(
                LinkAnnotation.Url(
                    legalNoticeUrl,
                    TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline))
                )
            ) {
                append(stringResource(R.string.terms_and_privacy))
            }
        },
        style = MaterialTheme.typography.bodyMedium
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun PreviewThirdRegistrationForm() {
    GedoiseTheme {
        Surface {
            ThirdRegistrationForm(
                email = "",
                password = "",
                loading = false,
                legalNoticeChecked = false,
                emailError = null,
                passwordError = null,
                errorMessage = null,
                onEmailChange = {},
                onPasswordChange = {},
                onLegalNoticeCheckedChange = {}
            )
        }
    }
}