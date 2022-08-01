package shareables.utils.config;

public enum ConfigFileIdentifier {
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
    GUI_WEEKLY_SCHEDULE("guiWeeklySchedulePath");

    private String configKeyString;

    private ConfigFileIdentifier(String configKeyString) {
        this.configKeyString = configKeyString;
    }

    public String getConfigKeyString() {
        return configKeyString;
    }
}
