package server.database.testdata.builders.departmentbuilders;

import shareables.models.pojos.abstractions.DepartmentName;
import shareables.utils.config.ConfigFileIdentifier;

public class MathDepartmentBuilder extends DepartmentBuilder {
    public MathDepartmentBuilder() {
        super(ConfigFileIdentifier.MATH_DEPARTMENT_INFO, DepartmentName.MATHEMATICS);
    }
}
