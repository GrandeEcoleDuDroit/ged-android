package com.upsaclay.message.data.model

internal object ConversationField {
    const val TABLE_NAME = "conversations"
    const val CONVERSATION_ID = "conversationId"
    const val CREATED_AT = "createdAt"
    const val DELETE_TIME = "deleteTime"

    object Remote {
        const val PARTICIPANTS = "participants"
    }

    object Local {
        const val INTERLOCUTOR_ID = "interlocutorId"
        const val INTERLOCUTOR_FIRST_NAME = "interlocutorFirstName"
        const val INTERLOCUTOR_LAST_NAME = "interlocutorLastName"
        const val INTERLOCUTOR_EMAIL = "interlocutorEmail"
        const val INTERLOCUTOR_SCHOOL_LEVEL = "interlocutorSchoolLevel"
        const val INTERLOCUTOR_ADMIN = "interlocutorAdmin"
        const val INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME = "interlocutorProfilePictureFileName"
        const val INTERLOCUTOR_STATE = "interlocutorState"
        const val INTERLOCUTOR_TESTER = "interlocutorTester"
        const val CONVERSATION_STATE = "conversationState"
        const val CONVERSATION_DELETE_TIME = "conversationDeleteTime"
    }
}