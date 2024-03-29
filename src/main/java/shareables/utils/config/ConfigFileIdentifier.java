package shareables.utils.config;

public enum ConfigFileIdentifier {
    CONSTANTS("constantsPath"),
    TEXTS("textsPath"),
    ACADEMIC_REQUEST_TEXTS("academicRequestTextsPath"),
    ADDRESSES("addressesPath"),
    NETWORK("networkPath"),
    ID_GENERATION("idGenerationPath"),

    ADMIN_INFO("adminInfoPath"),
    MR_MOHSENI_INFO("mrMohseniInfoPath"),

    MATH_DEPARTMENT_INFO("mathDepartmentInfoPath"),
    PHYSICS_DEPARTMENT_INFO("physicsDepartmentInfoPath"),
    GENERAL_CENTERS_DEPARTMENT_INFO("generalCentersDepartmentInfoPath"),

    GUI("guiPath"),
    GUI_LOGIN("guiLoginPath"),
    GUI_MAIN("guiMainPath"),
    GUI_STUDENT_MAIN("guiStudentMainPath"),
    GUI_PROFESSOR_MAIN("guiProfessorMainPath"),
    GUI_SPECIAL_USERS_MAIN("guiSpecialUsersMainPath"),
    GUI_PROFILE("guiProfilePath"),
    GUI_WEEKLY_SCHEDULE("guiWeeklySchedulePath"),
    GUI_EXAMS_LIST("guiExamsListPath"),
    GUI_DORM_SUBMISSION("guiDormSubmissionPath"),
    GUI_CERTIFICATE_SUBMISSION("guiCertificateSubmissionPath"),
    GUI_DEFENSE_SUBMISSION("guiDefenseSubmissionPath"),
    GUI_DROPPING_OUT_SUBMISSION("guiDroppingOutSubmissionPath"),
    GUI_LIST_VIEW("guiListViewPath"),
    GUI_LIST_MANAGER("guiListManagerPath"),
    GUI_LIST_EDITOR("guiListEditorPath"),
    GUI_COURSE_EDITOR("guiCourseEditorPath"),
    GUI_COURSE_ADDER("guiCourseAdderPath"),
    GUI_REQUEST_MANAGER("guiRequestManagerPath"),
    GUI_RECOMMENDATION_SUBMISSION("guiRecommendationSubmissionPath"),
    GUI_MINOR_SUBMISSION("guiMinorSubmissionPath"),
    GUI_CURRENT_STANDING("guiCurrentStandingViewPath"),
    GUI_TEMPORARY_STANDING_VIEW("guiTemporaryStandingViewPath"),
    GUI_TEMPORARY_STANDING_MANAGER("guiTemporaryStandingManagerPath"),
    GUI_TEMPORARY_STANDING_MASTER("guiTemporaryStandingMasterPath"),
    GUI_CURRENT_STANDING_MASTER("guiCurrentStandingMasterPath"),
    GUI_PROFESSOR_ADDER("guiProfessorAdderPath"),
    GUI_STUDENT_ADDER("guiStudentAdderPath"),
    GUI_PROFESSOR_EDITOR("guiProfessorEditorPath"),
    GUI_PANEL_TEMPLATE("guiPanelTemplatePath"),
    GUI_OFFLINE_PANEL("guiOfflinePanelPath"),
    GUI_MESSENGER_VIEW("guiMessengerViewPath"),
    GUI_CONVERSATION_ROOM("guiConversationRoomPath"),
    GUI_CONVERSATION_STARTER("guiConversationStarterPath"),
    GUI_CONVERSATION_STARTING_ROOM("guiConversationStartingRoomPath"),
    GUI_NOTIFICATIONS_VIEW("guiNotificationsViewPath"),
    GUI_MR_MOHSENI_SEARCHER("guiMrMohseniSearcherPath"),
    GUI_STUDENT_PROFILE_VIEW("guiStudentProfileViewPath"),
    GUI_UNIT_SELECTION_ADDER("guiUnitSelectionAdderPath"),
    GUI_UNIT_SELECTION_MENU("guiUnitSelectionMenuPath"),
    GUI_COURSES_SELECTION_PANEL("guiCoursesSelectionPanelPath"),
    GUI_COURSEWARES_VIEW("guiCoursewaresViewPath"),
    GUI_COURSE_MENU("guiCourseMenuPath"),
    GUI_MATERIALS_VIEW("guiMaterialsViewPath"),
    GUI_MATERIAL_ADDER("guiMaterialAdderPath"),
    GUI_MATERIAL_DISPLAY("guiMaterialDisplayPath"),
    GUI_HOMEWORKS_VIEW("guiHomeworksViewPath"),
    GUI_HOMEWORK_ADDER("guiHomeworkAdderPath"),
    GUI_HOMEWORK_DISPLAY("guiHomeworkDisplayPath"),
    GUI_HOMEWORK_MANAGER("guiHomeworkManagerPath"),
    GUI_CALENDAR_VIEW("guiCalendarViewPath");

    private String configKeyString;

    ConfigFileIdentifier(String configKeyString) {
        this.configKeyString = configKeyString;
    }

    public String getConfigKeyString() {
        return configKeyString;
    }
}
