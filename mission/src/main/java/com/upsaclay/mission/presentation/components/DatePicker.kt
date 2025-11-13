package com.upsaclay.mission.presentation.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.LocalDateFormatter
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import java.time.LocalDate

@Composable
fun OutlinedDatePicker(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onClick: () -> Unit,
    label: String,
) {
    SimpleOutlinedTextField(
        modifier = modifier
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        onClick()
                    }
                }
            },
        value = LocalDateFormatter.formatDayMonthYear(date),
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

@PhonePreviews
@Composable
private fun OutlinedDatePickerPreview() {
    GedoiseTheme {
        Surface {
            OutlinedDatePicker(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding)),
                date = LocalDate.now(),
                onClick = {},
                label = stringResource(R.string.start_date)
            )
        }
    }
}
