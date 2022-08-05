package shareables.network.requests;

public enum RequestIdentifier {
    LOG_IN,
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
    CHANGE_COURSE_LEVEL,
    REMOVE_COURSE,
    ADD_COURSE,
    ADD_PROFESSOR,
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
    GET_COURSE_STATS_DTO
}