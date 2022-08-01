package server.network.clienthandling.logicutils;

import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.academicrequests.DormRequest;

public class AcademicRequestUtils {
    public static boolean willGetDorm() {
        DormRequest dormRequest = new DormRequest();
        return dormRequest.getRequestStatus() == AcademicRequestStatus.ACCEPTED;
    }
}
