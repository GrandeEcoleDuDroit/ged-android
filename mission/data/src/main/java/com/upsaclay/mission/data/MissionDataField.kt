package com.upsaclay.mission.data

object MissionField {
    object Local {
        const val MISSION_TABLE_NAME = "missions"
        const val MISSION_ID = "mission_id"
        const val MISSION_TITLE = "mission_title"
        const val MISSION_DESCRIPTION = "mission_description"
        const val MISSION_SCHOOL_LEVELS = "mission_school_levels"
        const val MISSION_DATE = "mission_date"
        const val MISSION_START_DATE = "mission_start_date"
        const val MISSION_END_DATE = "mission_end_date"
        const val MISSION_DURATION = "mission_duration"
        const val MISSION_MANAGERS = "mission_managers"
        const val MISSION_PARTICIPANTS = "mission_participants"
        const val MISSION_MAX_PARTICIPANTS = "mission_max_participants"
        const val MISSION_TASKS = "mission_tasks"
        const val MISSION_IMAGE_REFERENCE = "mission_image_reference"
        const val MISSION_STATE = "mission_state"
    }

    object Remote {
        const val MISSION_ID = "MISSION_ID"
        const val MISSION_TITLE = "MISSION_TITLE"
        const val MISSION_DESCRIPTION = "MISSION_DESCRIPTION"
        const val MISSION_SCHOOL_LEVELS = "MISSION_SCHOOL_LEVELS"
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

object MissionTaskField {
    object Local {
        const val MISSION_TASK_ID = "mission_task_id"
        const val MISSION_TASK_VALUE = "mission_task_value"
    }
    object Remote {
        const val MISSION_TASK_ID = "MISSION_TASK_ID"
        const val MISSION_TASK_VALUE = "MISSION_TASK_VALUE"
    }
}