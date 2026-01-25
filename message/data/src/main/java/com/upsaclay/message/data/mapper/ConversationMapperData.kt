package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.extensions.formatUrl
import com.upsaclay.common.data.extensions.toLocalDateTime
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.common.domain.UserUtils
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.User.UserState
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.common.domain.extensions.capitalize
import com.upsaclay.message.data.local.model.LocalConversation
import com.upsaclay.message.data.model.ConversationField.Remote.CONVERSATION_ID
import com.upsaclay.message.data.model.ConversationField.Remote.CREATED_AT
import com.upsaclay.message.data.model.ConversationField.Remote.EFFECTIVE_FROM
import com.upsaclay.message.data.model.ConversationField.Remote.PARTICIPANTS
import com.upsaclay.message.data.remote.model.RemoteConversation
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Conversation.ConversationState
import com.upsaclay.message.domain.entity.ConversationDTO

fun Conversation.toLocal() = LocalConversation(
    conversationId = id,
    conversationState = state.name,
    conversationEffectiveFrom = effectiveFrom?.toEpochMilliUTC(),
    conversationCreatedAt = createdAt.toEpochMilliUTC(),
    conversationInterlocutorId = interlocutor.id,
    conversationInterlocutorFirstName = interlocutor.firstName.lowercase(),
    conversationInterlocutorLastName = interlocutor.lastName.lowercase(),
    conversationInterlocutorEmail = interlocutor.email,
    conversationInterlocutorAdmin = interlocutor.admin,
    conversationInterlocutorSchoolLevel = interlocutor.schoolLevel.number,
    conversationInterlocutorProfilePictureFileName = UserUtils.ProfilePicture.getFileName(interlocutor.profilePictureUrl),
    conversationInterlocutorState = interlocutor.state.number,
    conversationInterlocutorTester = interlocutor.tester
)

internal fun Conversation.toRemote(userId: String) = RemoteConversation(
    conversationId = id,
    participants = listOf(userId, interlocutor.id),
    createdAt = createdAt.toTimestamp(),
    effectiveFrom = effectiveFrom?.let { mapOf(userId to it.toTimestamp()) }
)

fun LocalConversation.toConversation(): Conversation {
    val interlocutor = User(
        id = conversationInterlocutorId,
        firstName = conversationInterlocutorFirstName.capitalize(),
        lastName = conversationInterlocutorLastName.capitalize(),
        email = conversationInterlocutorEmail,
        schoolLevel = SchoolLevel.fromNumber(conversationInterlocutorSchoolLevel),
        admin = conversationInterlocutorAdmin,
        profilePictureUrl = UserUtils.ProfilePicture.formatUrl(conversationInterlocutorProfilePictureFileName),
        state = UserState.fromNumber(conversationInterlocutorState),
        tester = conversationInterlocutorTester
    )

    return Conversation(
        id = conversationId,
        interlocutor = interlocutor,
        createdAt = conversationCreatedAt.toLocalDateTimeUTC(),
        state = ConversationState.valueOf(conversationState),
        effectiveFrom = conversationEffectiveFrom?.toLocalDateTimeUTC()
    )
}

internal fun RemoteConversation.toDTO(userId: String) = ConversationDTO(
    conversationId = conversationId,
    participants = participants,
    createdAt = createdAt.toLocalDateTime(),
    effectiveFrom = effectiveFrom?.get(userId)?.toLocalDateTime()
)

internal fun RemoteConversation.toMap(): Map<String, Any> {
    val data = mutableMapOf<String, Any>()
    data[CONVERSATION_ID] = conversationId
    data[PARTICIPANTS] = participants
    data[CREATED_AT] = createdAt
    effectiveFrom?.let {
        data[EFFECTIVE_FROM] = it
    }
    return data
}