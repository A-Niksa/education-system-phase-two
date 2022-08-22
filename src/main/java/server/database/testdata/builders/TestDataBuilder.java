package server.database.testdata.builders;

import server.database.management.DatabaseManager;
import server.database.management.DatabaseWriter;
import server.database.testdata.builders.departmentbuilders.DepartmentBuilder;
import server.database.testdata.builders.departmentbuilders.GeneralCentersDepartmentBuilder;
import server.database.testdata.builders.departmentbuilders.MathDepartmentBuilder;
import server.database.testdata.builders.departmentbuilders.PhysicsDepartmentBuilder;
import server.database.testdata.builders.userbuilders.SpecialUsersBuilder;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigIdSupplier;
import shareables.utils.config.ConfigManager;
import shareables.utils.logging.MasterLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestDataBuilder {
    private DatabaseManager databaseManager;
    private List<DepartmentBuilder> departmentBuilders;
    private SpecialUsersBuilder specialUsersBuilder;

    public TestDataBuilder() {
        databaseManager = new DatabaseManager();
        specialUsersBuilder = new SpecialUsersBuilder();
        initializeDepartmentBuilders();
    }

    private void initializeDepartmentBuilders() {
        departmentBuilders = new ArrayList<>();
        departmentBuilders.add(new MathDepartmentBuilder());
        departmentBuilders.add(new PhysicsDepartmentBuilder());
        departmentBuilders.add(new GeneralCentersDepartmentBuilder());
    }

    public void build() {
        purgeDatabase();

        departmentBuilders.forEach(e -> e.buildDepartment(databaseManager));
        specialUsersBuilder.buildSpecialUsers(databaseManager);

        databaseManager.saveDatabase();

        MasterLogger.serverInfo("TEST DATA HAS BEEN BUILT", "build", getClass());
    }

    private void purgeDatabase() {
        ConfigIdSupplier.resetCurrentClientId();

        DatabaseWriter databaseWriter = databaseManager.getDatabaseWriter();

        File datasetsFolder = new File(ConfigManager.getString(ConfigFileIdentifier.ADDRESSES,
                "datasetsFolderPath"));
        databaseWriter.purgeDirectory(datasetsFolder);

        File localDatasetsFolder = new File(ConfigManager.getString(ConfigFileIdentifier.ADDRESSES,
                "localDatasetsFolderPath"));
        databaseWriter.purgeDirectoryCompletely(localDatasetsFolder);

        File downloadsFolder = new File(ConfigManager.getString(ConfigFileIdentifier.ADDRESSES,
                "downloadsFolderPath"));
        databaseWriter.purgeDirectoryCompletely(downloadsFolder);
    }
}
