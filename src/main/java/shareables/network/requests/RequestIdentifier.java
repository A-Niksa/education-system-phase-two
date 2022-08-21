package shareables.network.requests;

public enum RequestIdentifier {
    LOG_IN,
    CONNECTION_PING,
    GET_OFFLINE_MODE_DTO,
    CHANGE_PASSWORD,
    GET_USER,
    GET_ADVISING_PROFESSOR_NAME,
    CHANGE_EMAIL_ADDRESS,
    CHANGE_PHONE_NUMBER,
    GET_STUDENT_COURSE_DTOS,
    GET_PROFESSOR_COURSE_DTOS,
    GET_DEPARTMENT_COURSE_DTOS,
    GET_DEPARTMENT_PROFESSOR_DTOS,
    GET_ACTIVE_COURSE_DTOS,
    GET_PROFESSOR_DTOS,
    ASK_FOR_DORM,
    ASK_FOR_CERTIFICATE,
    GET_DEFENSE_TIME,
    ASK_FOR_DEFENSE_TIME,
    GET_DROPPING_OUT_SUBMISSION_STATUS,
    ASK_FOR_DROPPING_OUT,
    ASK_FOR_RECOMMENDATION,
    GET_STUDENT_RECOMMENDATION_TEXTS,
    GET_PROFESSOR_RECOMMENDATION_REQUEST_DTOS,
    GET_DEPARTMENT_DROPPING_OUT_REQUEST_DTOS,
    ACCEPT_RECOMMENDATION_REQUEST,
    DECLINE_RECOMMENDATION_REQUEST,
    ACCEPT_DROPPING_OUT_REQUEST,
    DECLINE_DROPPING_OUT_REQUEST,
    GET_STUDENT_MINOR_REQUEST_DTOS,
    ASK_FOR_MINOR,
    GET_PROFESSOR_MINOR_REQUEST_DTOS,
    ACCEPT_MINOR_REQUEST,
    DECLINE_MINOR_REQUEST,
    CHANGE_COURSE_NAME,
    CHANGE_COURSE_TEACHING_PROFESSORS,
    CHANGE_COURSE_NUMBER_OF_CREDITS,
    CHANGE_COURSE_DEGREE_LEVEL,
    REMOVE_COURSE,
    CHANGE_PROFESSOR_ACADEMIC_LEVEL,
    CHANGE_PROFESSOR_OFFICE_NUMBER,
    DEMOTE_FROM_DEPUTY,
    PROMOTE_TO_DEPUTY,
    ADD_COURSE,
    ADD_PROFESSOR,
    REMOVE_PROFESSOR,
    ADD_STUDENT,
    GET_STUDENT_COURSE_SCORE_DTOS_WITH_ID,
    GET_STUDENT_COURSE_SCORE_DTOS_WITH_NAME,
    GET_STUDENT_TEMPORARY_COURSE_SCORE_DTOS,
    GET_STUDENT_TRANSCRIPT_DTO_WITH_ID,
    GET_STUDENT_TRANSCRIPT_DTO_WITH_NAME,
    SUBMIT_PROTEST,
    GET_PROFESSOR_ACTIVE_COURSE_NAMES,
    GET_COURSE_SCORE_DTOS_FOR_COURSE,
    RESPOND_TO_PROTEST,
    SAVE_TEMPORARY_SCORES,
    FINALIZE_SCORES,
    GET_COURSE_SCORE_DTOS_FOR_PROFESSOR,
    GET_COURSE_SCORE_DTOS_FOR_STUDENT,
    GET_DEPARTMENT_STUDENT_IDS,
    GET_DEPARTMENT_STUDENT_NAMES,
    GET_DEPARTMENT_COURSE_NAMES,
    GET_DEPARTMENT_PROFESSOR_NAMES,
    GET_COURSE_STATS_DTO,
    GET_CONVERSATION_THUMBNAIL_DTOS,
    GET_CONTACT_CONVERSATION_DTO,
    SEND_TEXT_MESSAGE,
    SEND_MEDIA_MESSAGE,
    DOWNLOAD_MEDIA_FROM_CONVERSATION,
    DOWNLOAD_MEDIA_FROM_EDUCATIONAL_MATERIAL,
    GET_STUDENT_CONTACT_PROFILE_DTOS,
    GET_PROFESSOR_CONTACT_PROFILE_DTOS,
    CHECK_IF_CONTACT_IDS_EXIST,
    SEND_MESSAGE_NOTIFICATIONS_IF_NECESSARY,
    GET_NOTIFICATION_DTOS,
    ACCEPT_NOTIFICATION,
    DECLINE_NOTIFICATION,
    GET_ALL_STUDENT_CONTACT_PROFILE_DTOS,
    GET_FILTERED_STUDENT_CONTACT_PROFILE_DTOS,
    GET_STUDENT_DTO,
    GET_MR_MOHSENI_CONTACT_PROFILE_DTOS,
    GET_FILTERED_CONTACT_IDS,
    ADD_UNIT_SELECTION_SESSION,
    GET_DEPARTMENT_COURSE_THUMBNAIL_DTOS,
    GET_DEPARTMENT_COURSE_THUMBNAIL_DTOS_ALPHABETICALLY,
    GET_DEPARTMENT_COURSE_THUMBNAIL_DTOS_IN_EXAM_DATE_ORDER,
    GET_DEPARTMENT_COURSE_THUMBNAIL_DTOS_IN_DEGREE_LEVEL_ORDER,
    ACQUIRE_COURSE,
    REMOVE_ACQUIRED_COURSE,
    PIN_COURSE_TO_FAVORITES,
    UNPIN_COURSE_FROM_FAVORITES,
    REQUEST_COURSE_ACQUISITION,
    GET_COURSE_GROUPS_THUMBNAIL_DTOS,
    CHANGE_GROUP_NUMBER,
    GET_PINNED_COURSE_THUMBNAIL_DTOS,
    GET_STUDENT_COURSEWARE_THUMBNAIL_DTOS,
    GET_PROFESSOR_COURSEWARE_THUMBNAIL_DTOS,
    GET_CALENDAR_EVENT_DTOS,
    GET_MATERIAL_THUMBNAIL_DTOS,
    ADD_STUDENT_TO_COURSE,
    ADD_TA_TO_COURSE,
    ADD_EDUCATIONAL_MATERIAL_ITEMS,
    EDIT_EDUCATIONAL_MATERIAL_ITEMS,
    GET_COURSE_MATERIAL_EDUCATIONAL_ITEMS,
    REMOVE_COURSE_EDUCATIONAL_MATERIAL,
    REMOVE_ALL_COURSE_EDUCATIONAL_MATERIALS,
    GET_TEACHING_ASSISTANCE_STATUS
}