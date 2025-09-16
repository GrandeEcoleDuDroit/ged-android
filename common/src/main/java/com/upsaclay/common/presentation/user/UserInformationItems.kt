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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.gold
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.Phones

@Composable
fun UserInformationItems(user: User) {
    val accountInfos: List<AccountInfo> = listOf(
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.last_name),
            user.lastName
        ),
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.first_name),
            user.firstName
        ),
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.email),
            user.email
        ),
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.school_level),
            user.schoolLevel
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
    ) {
        accountInfos.forEach { accountInfo ->
            NonMemberUserInformationItem(
                accountInfo = accountInfo
            )
        }

        if (user.isMember) {
            MemberUserInformationItem(
                modifier = Modifier
                    .testTag(stringResource(id = R.string.member_text_tag))
            )
        }
    }
}

@Composable
private fun MemberUserInformationItem(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding))
    ) {
        Text(
            text = stringResource(R.string.member),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
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

@Composable
internal fun NonMemberUserInformationItem(
    accountInfo: AccountInfo
) {
    Column {
        Text(
            text = accountInfo.label,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = accountInfo.value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


data class AccountInfo(
    val label: String,
    val value: String
)

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AccountInfoItemsPreview() {
    GedoiseTheme {
        Surface {
            UserInformationItems(user = userFixture)
        }
    }
}