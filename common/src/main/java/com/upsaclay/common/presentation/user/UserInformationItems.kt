package com.upsaclay.common.presentation.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.gold
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun UserInformationItems(user: User) {
    val accountInformationValues: List<AccountInformationValue> = listOf(
        AccountInformationValue(
            stringResource(id = com.upsaclay.common.R.string.last_name),
            user.lastName
        ),
        AccountInformationValue(
            stringResource(id = com.upsaclay.common.R.string.first_name),
            user.firstName
        ),
        AccountInformationValue(
            stringResource(id = com.upsaclay.common.R.string.email),
            user.email
        ),
        AccountInformationValue(
            stringResource(id = com.upsaclay.common.R.string.school_level),
            user.schoolLevel.value
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
    ) {
        accountInformationValues.forEach { accountInfo ->
            UserInformationItem(
                accountInformationValue = accountInfo
            )
        }

        if (user.admin) {
            Row(
                modifier = Modifier.testTag(stringResource(id = R.string.member_text_tag)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding))
            ) {
                Text(
                    text = stringResource(R.string.administrator),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.gold,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
internal fun UserInformationItem(
    accountInformationValue: AccountInformationValue
) {
    Column {
        Text(
            text = accountInformationValue.label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )

        Text(text = accountInformationValue.value)
    }
}


data class AccountInformationValue(
    val label: String,
    val value: String
)

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun AccountInfoItemsPreview() {
    GedoiseTheme {
        Surface {
            UserInformationItems(user = userFixture)
        }
    }
}