package com.upsaclay.message.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.upsaclay.message.data.model.MessageField.Remote.TIMESTAMP

fun CollectionReference.withOffsetTime(offsetTime: Timestamp?): Query {
    return offsetTime?.let {
        whereGreaterThanOrEqualTo(TIMESTAMP, it)
    } ?: this
}