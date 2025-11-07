package com.upsaclay.mission.data

object MissionField {
    object Local {
        const val MISSION_ID = "missionId"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val SCHOOL_LEVELS = "schoolLevels"
        const val DATE = "date"
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val DURATION = "duration"
        const val MANAGERS = "managers"
        const val PARTICIPANTS = "participants"
        const val MAX_PARTICIPANTS = "maxParticipants"
        const val TASKS = "tasks"
        const val IMAGE_REFERENCE = "imageReference"
        const val STATE = "state"
    }

    object Remote {
        const val MISSION_ID = "MISSION_ID"
        const val MISSION_TITLE = "MISSION_TITLE"
        const val MISSION_DESCRIPTION = "MISSION_DESCRIPTION"
        const val MISSION_SCHOOL_LEVEL = "MISSION_SCHOOL_LEVEL"
        const val MISSION_DATE = "MISSION_DATE"
        const val MISSION_START_DATE = "MISSION_START_DATE"
        const val MISSION_END_DATE = "MISSION_END_DATE"
        const val MISSION_DURATION = "MISSION_DURATION"
        const val MISSION_MAX_PARTICIPANTS = "MISSION_MAX_PARTICIPANTS"
        const val MISSION_IMAGE_FILE_NAME = "MISSION_IMAGE_FILE_NAME"
        const val MISSION_TASKS = "MISSION_TASKS"

        object Inbound {
            const val MISSION_MANAGERS = "MISSION_MANAGERS"
            const val MISSION_PARTICIPANTS = "MISSION_PARTICIPANTS"
        }

        object Outbound {
            const val MISSION_MANAGER_IDS = "MISSION_MANAGER_IDS"
            const val MISSION_PARTICIPANT_IDS = "MISSION_PARTICIPANT_IDS"
        }
    }
}