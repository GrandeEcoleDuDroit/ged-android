package com.upsaclay.message.data.model

internal object ConversationField {
    const val CONVERSATION_TABLE_NAME = "conversations"

    object Remote {
        const val CONVERSATION_ID = "conversationId"
        const val CREATED_AT = "createdAt"
        const val DELETE_TIME = "deleteTime"
        const val PARTICIPANTS = "participants"
    }

    object Local {
        const val CONVERSATION_ID = "conversation_id"
        const val CONVERSATION_CREATED_AT = "conversation_created_at"
        const val CONVERSATION_STATE = "conversation_state"
        const val CONVERSATION_DELETE_TIME = "conversation_delete_time"
        const val CONVERSATION_INTERLOCUTOR_ID = "conversation_interlocutor_id"
        const val CONVERSATION_INTERLOCUTOR_FIRST_NAME = "conversation_interlocutor_first_name"
        const val CONVERSATION_INTERLOCUTOR_LAST_NAME = "conversation_interlocutor_last_name"
        const val CONVERSATION_INTERLOCUTOR_EMAIL = "conversation_interlocutor_email"
        const val CONVERSATION_INTERLOCUTOR_SCHOOL_LEVEL = "conversation_interlocutor_school_level"
        const val CONVERSATION_INTERLOCUTOR_ADMIN = "conversation_interlocutor_admin"
        const val CONVERSATION_INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME = "conversation_interlocutor_profile_picture_file_name"
        const val CONVERSATION_INTERLOCUTOR_STATE = "conversation_interlocutor_state"
        const val CONVERSATION_INTERLOCUTOR_TESTER = "conversation_interlocutor_tester"
    }
}