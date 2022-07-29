package server.database.models.pojos.abstractions;

public enum DepartmentName {
    MATHEMATICS("Mathematics"),
    PHYSICS("Physics"),
    ECONOMICS("Economics"),
    CHEMISTRY("Chemistry"),
    AEROSPACE_ENGINEERING("Aerospace Engineering");

    private String departmentNameString;

    private DepartmentName(String departmentNameString) {
        this.departmentNameString = departmentNameString;
    }

    @Override
    public String toString() {
        return departmentNameString;
    }
}
