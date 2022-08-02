package shareables.utils.config;

public enum ConfigFileIdentifier { // TODO: unrelated to here but putting fatals for exceptions
    CONSTANTS("constantsPath"),
    TEXTS("textsPath"),
    ADDRESSES("addressesPath"),
    NETWORK("networkPath"),
    GUI("guiPath"),
    GUI_LOGIN("guiLoginPath"),
    GUI_MAIN("guiMainPath"),
    GUI_STUDENT_MAIN("guiStudentMainPath"),
    GUI_PROFESSOR_MAIN("guiProfessorMainPath"),
    GUI_PROFILE("guiProfilePath"),
    GUI_WEEKLY_SCHEDULE("guiWeeklySchedulePath"),
    GUI_EXAMS_LIST("guiExamsListPath"),
    GUI_DORM_SUBMISSION("guiDormSubmission"),
    GUI_CERTIFICATE_SUBMISSION("guiCertificateSubmissionPath"),
    GUI_LIST_VIEW("guiListView"),
    GUI_LIST_MANAGER("guiListManager"),
    GUI_LIST_EDITOR("guiListEditor"),
    GUI_COURSE_EDITOR("guiCourseEditor"),
    GUI_COURSE_ADDER("guiCourseAdder");

    private String configKeyString;

    private ConfigFileIdentifier(String configKeyString) {
        this.configKeyString = configKeyString;
    }

    public String getConfigKeyString() {
        return configKeyString;
    }
}
