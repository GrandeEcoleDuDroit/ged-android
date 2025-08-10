package com.upsaclay.common.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Phone",
    group = "phones",
    device = "spec:width=411dp,height=891dp",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)

@Preview(
    name = "Phone Dark",
    group = "phones",
    device = "spec:width=411dp,height=891dp",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class Phones

@Preview(
    name = "Tablet",
    group = "tablets",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Tablet Dark",
    group = "tablets",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class Tablets
