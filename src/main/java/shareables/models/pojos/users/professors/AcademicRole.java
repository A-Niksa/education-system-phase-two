package shareables.models.pojos.users.professors;

public enum AcademicRole {
    DEAN("Dean"),
    DEPUTY("Education Deputy"),
    NORMAL("No Admin Role");

    private String academicRoleString;

    AcademicRole(String academicRoleString) {
        this.academicRoleString = academicRoleString;
    }

    @Override
    public String toString() {
        return academicRoleString;
    }
}
