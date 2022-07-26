package server.database.datamodels.users;

public enum UserIdentifier {
    PROFESSOR("professor"),
    STUDENT("student");

    private String identifierString;

    private UserIdentifier(String identifierString) {
        this.identifierString = identifierString;
    }

    @Override
    public String toString() {
        return identifierString;
    }
}
