package com.upsaclay.gedoise.presentation.profile.supportContact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.upsaclay.common.R as commonR
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.gedoise.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun SupportContactDestination(
    onBackClick: () -> Unit,
    viewModel: SupportContactViewModel = koinViewModel()
)
: Unit {
    SupportContactScreen(
        onBackClick = onBackClick,
        onObjetChange = viewModel::onObjetChange,
        onMessageChange = viewModel::onMessageChange,
        onSendMail = viewModel::sendMail
    )
    
}

@Composable
fun SupportContactScreen(
    onBackClick : () -> Unit,
    onObjetChange : (String) -> Unit,
    onMessageChange : (String) -> Unit,
    onSendMail : () -> Unit
){
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.support)
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SimpleOutlinedTextField(
                    value = stringResource(R.string.objet),
                    label = stringResource(R.string.objet),
                    onValueChange = onObjetChange,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                SimpleOutlinedTextField(
                    value = stringResource(R.string.message),
                    label = stringResource(R.string.message),
                    onValueChange = onMessageChange,
                    modifier = Modifier
                        .fillMaxHeight(0.80f)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                PrimaryButton(
                    modifier = Modifier.testTag(stringResource(R.string.send_support_mail_button_tag)),
                    text = stringResource(R.string.send_support_mail_button),
                    onClick = onSendMail
                )
            }
        }
    }
}

@Preview
@Composable
fun SupportContactScreenPreview(): Unit {
    GedoiseTheme {
        SupportContactScreen(
            onBackClick = {},
            onMessageChange = { },
            onObjetChange = { },
            onSendMail = { }
        )
    }
}