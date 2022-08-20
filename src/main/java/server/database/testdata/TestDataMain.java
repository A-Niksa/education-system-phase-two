package server.database.testdata;

import server.database.testdata.builders.TestDataBuilder;
import shareables.utils.logging.MasterLogger;

public class TestDataMain {
    public static void main(String[] args) {
        MasterLogger.serverInfo("Start of test data program", "psvm", TestDataMain.class);
        new TestDataBuilder().build();
    }
}
