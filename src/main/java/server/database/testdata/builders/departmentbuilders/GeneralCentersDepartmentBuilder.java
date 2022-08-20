package server.database.testdata.builders.departmentbuilders;

import shareables.models.pojos.abstractions.DepartmentName;
import shareables.utils.config.ConfigFileIdentifier;

public class GeneralCentersDepartmentBuilder extends DepartmentBuilder {
    public GeneralCentersDepartmentBuilder() {
        super(ConfigFileIdentifier.GENERAL_CENTERS_DEPARTMENT_INFO, DepartmentName.GENERAL_CENTERS);
    }
}
