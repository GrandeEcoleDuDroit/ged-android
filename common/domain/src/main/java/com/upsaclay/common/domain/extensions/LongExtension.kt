package com.upsaclay.common.domain.extensions

import com.upsaclay.common.domain.entity.ByteUnit

fun Long.toBytes(unit: ByteUnit) = this / unit.value