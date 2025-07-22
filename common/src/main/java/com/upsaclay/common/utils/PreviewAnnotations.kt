package com.upsaclay.common.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Phone",
    group = "phones",
    device = Devices.PHONE,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)

@Preview(
    name = "Phone Dark",
    group = "phones",
    device = Devices.PHONE,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class Phones

@Preview(
    name = "Tablet",
    group = "tablets",
    device = Devices.TABLET,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Tablet Dark",
    group = "tablets",
    device = Devices.TABLET,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class Tablets
