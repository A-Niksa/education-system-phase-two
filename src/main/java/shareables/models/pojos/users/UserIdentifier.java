package shareables.models.pojos.users;

public enum UserIdentifier {
    PROFESSOR("professor"),
    STUDENT("student"),
    MR_MOHSENI("mr. mohseni"),
    ADMIN("admin");

    private String identifierString;

    private UserIdentifier(String identifierString) {
        this.identifierString = identifierString;
    }

    @Override
    public String toString() {
        return identifierString;
    }
}
