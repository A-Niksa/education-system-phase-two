package shareables.models.pojos.abstractions;

public enum DepartmentName {
    MATHEMATICS("Mathematics"),
    PHYSICS("Physics"),
    ECONOMICS("Economics"),
    CHEMISTRY("Chemistry"),
    AEROSPACE_ENGINEERING("Aerospace Engineering"),
    GENERAL_CENTERS("General Centers");

    private String departmentNameString;

    private DepartmentName(String departmentNameString) {
        this.departmentNameString = departmentNameString;
    }

    @Override
    public String toString() {
        return departmentNameString;
    }
}
