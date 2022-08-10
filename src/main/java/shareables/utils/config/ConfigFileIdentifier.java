package shareables.utils.config;

public enum ConfigFileIdentifier { // TODO: unrelated to here but putting fatals for exceptions
    CONSTANTS("constantsPath"),
    TEXTS("textsPath"),
    ACADEMIC_REQUEST_TEXTS("academicRequestTextsPath"),
    ADDRESSES("addressesPath"),
    NETWORK("networkPath"),
    ID_GENERATION("idGenerationPath"),

    GUI("guiPath"),
    GUI_LOGIN("guiLoginPath"),
    GUI_MAIN("guiMainPath"),
    GUI_STUDENT_MAIN("guiStudentMainPath"),
    GUI_PROFESSOR_MAIN("guiProfessorMainPath"),
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
    GUI_MESSENGER_VIEW("guiMessengerViewPath");

    private String configKeyString;

    private ConfigFileIdentifier(String configKeyString) {
        this.configKeyString = configKeyString;
    }

    public String getConfigKeyString() {
        return configKeyString;
    }
}
