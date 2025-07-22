package com.upsaclay.forum.presentation.createmission.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.FormatLocalDateTimeUseCase
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import java.time.LocalDateTime

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    onClick: () -> Unit,
    label: String
) {
    SimpleOutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        onClick()
                    }
                }
            },
        value = FormatLocalDateTimeUseCase.formatDayMonthYear(date),
        onValueChange = {},
        readOnly = true,
        label = label,
        leadingIcon = {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_fill_calendar),
                contentDescription = null,
            )
        }
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun DatePickerFieldPreview() {
    GedoiseTheme {
        Surface {
            DatePicker(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                date = LocalDateTime.now(),
                onClick = {},
                label = stringResource(R.string.start_date)
            )
        }
    }
}
