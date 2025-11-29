package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.UrlUtils
import com.upsaclay.common.data.extensions.toLocalDateTime
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.User.UserState
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.common.domain.extensions.uppercaseFirstLetter
import com.upsaclay.message.data.local.model.LocalConversation
import com.upsaclay.message.data.model.ConversationField.Remote.CONVERSATION_ID
import com.upsaclay.message.data.model.ConversationField.Remote.CREATED_AT
import com.upsaclay.message.data.model.ConversationField.Remote.DELETE_TIME
import com.upsaclay.message.data.model.ConversationField.Remote.PARTICIPANTS
import com.upsaclay.message.data.remote.model.RemoteConversation
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Conversation.ConversationState

fun Conversation.toLocal() = LocalConversation(
    conversationId = id,
    conversationInterlocutorId = interlocutor.id,
    conversationInterlocutorFirstName = interlocutor.firstName.lowercase(),
    conversationInterlocutorLastName = interlocutor.lastName.lowercase(),
    conversationInterlocutorEmail = interlocutor.email,
    conversationInterlocutorAdmin = interlocutor.admin,
    conversationInterlocutorSchoolLevel = interlocutor.schoolLevel.number,
    conversationInterlocutorProfilePictureFileName = UrlUtils.extractFileNameFromUrl(interlocutor.profilePictureUrl),
    conversationInterlocutorState = interlocutor.state.toString(),
    conversationInterlocutorTester = interlocutor.tester,
    conversationCreatedAt = createdAt.toEpochMilliUTC(),
    conversationState = state.name,
    conversationDeleteTime = deleteTime?.toEpochMilliUTC()
)

internal fun Conversation.toRemote(userId: String) = RemoteConversation(
    conversationId = id,
    participants = listOf(userId, interlocutor.id),
    createdAt = createdAt.toTimestamp(),
    deleteTime = deleteTime?.let { mapOf(userId to it.toTimestamp()) }
)

fun LocalConversation.toConversation(): Conversation {
    val interlocutor = User(
        id = conversationInterlocutorId,
        firstName = conversationInterlocutorFirstName.uppercaseFirstLetter(),
        lastName = conversationInterlocutorLastName.uppercaseFirstLetter(),
        email = conversationInterlocutorEmail,
        schoolLevel = SchoolLevel.fromNumber(conversationInterlocutorSchoolLevel),
        admin = conversationInterlocutorAdmin,
        profilePictureUrl = UrlUtils.formatOracleBucketUrl(conversationInterlocutorProfilePictureFileName),
        state = UserState.fromString(conversationInterlocutorState),
        tester = conversationInterlocutorTester
    )

    return Conversation(
        id = conversationId,
        interlocutor = interlocutor,
        createdAt = conversationCreatedAt.toLocalDateTimeUTC(),
        state = ConversationState.valueOf(conversationState),
        deleteTime = conversationDeleteTime?.toLocalDateTimeUTC()
    )
}

internal fun RemoteConversation.toConversation(userId: String, interlocutor: User) = Conversation(
    id = conversationId,
    interlocutor = interlocutor,
    state = ConversationState.CREATED,
    createdAt = createdAt.toLocalDateTime(),
    deleteTime = deleteTime?.get(userId)?.toLocalDateTime()
)

internal fun RemoteConversation.toMap(): Map<String, Any> {
    val data = mutableMapOf<String, Any>()
    data[CONVERSATION_ID] = conversationId
    data[PARTICIPANTS] = participants
    data[CREATED_AT] = createdAt
    deleteTime?.let {
        data[DELETE_TIME] = it
    }
    return data
}