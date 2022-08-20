package server.database.testdata.builders.departmentbuilders;

import shareables.models.pojos.abstractions.DepartmentName;
import shareables.utils.config.ConfigFileIdentifier;

public class PhysicsDepartmentBuilder extends DepartmentBuilder {
    public PhysicsDepartmentBuilder() {
        super(ConfigFileIdentifier.PHYSICS_DEPARTMENT_INFO, DepartmentName.PHYSICS);
    }
}
