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
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.remote.model.RemoteConversation
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Conversation.ConversationState

fun Conversation.toLocal() = LocalConversation(
    conversationId = id,
    interlocutorId = interlocutor.id,
    interlocutorFirstName = interlocutor.firstName.lowercase(),
    interlocutorLastName = interlocutor.lastName.lowercase(),
    interlocutorEmail = interlocutor.email,
    interlocutorAdmin = interlocutor.admin,
    interlocutorSchoolLevel = interlocutor.schoolLevel.number,
    interlocutorProfilePictureFileName = UrlUtils.extractFileName(interlocutor.profilePictureUrl),
    interlocutorState = interlocutor.state.toString(),
    interlocutorTester = interlocutor.tester,
    createdAt = createdAt.toEpochMilliUTC(),
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
        id = interlocutorId,
        firstName = interlocutorFirstName.uppercaseFirstLetter(),
        lastName = interlocutorLastName.uppercaseFirstLetter(),
        email = interlocutorEmail,
        schoolLevel = SchoolLevel.fromNumber(interlocutorSchoolLevel),
        admin = interlocutorAdmin,
        profilePictureUrl = UrlUtils.formatOracleBucketUrl(interlocutorProfilePictureFileName),
        state = UserState.fromString(interlocutorState),
        tester = interlocutorTester
    )

    return Conversation(
        id = conversationId,
        interlocutor = interlocutor,
        createdAt = createdAt.toLocalDateTimeUTC(),
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
    data[ConversationField.CONVERSATION_ID] = conversationId
    data[ConversationField.Remote.PARTICIPANTS] = participants
    data[ConversationField.CREATED_AT] = createdAt
    deleteTime?.let {
        data[ConversationField.DELETE_TIME] = it
    }
    return data
}