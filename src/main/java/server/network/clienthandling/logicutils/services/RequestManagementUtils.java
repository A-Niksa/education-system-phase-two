package server.network.clienthandling.logicutils.services;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.academicrequests.DroppingOutRequest;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;
import shareables.network.DTOs.RequestDTO;

import java.util.ArrayList;
import java.util.List;

public class RequestManagementUtils {
    public static List<RequestDTO> getDroppingOutRequestDTOs(DatabaseManager databaseManager, String departmentId) {
        List<Identifiable> droppingOutRequests = databaseManager.getIdentifiables(DatasetIdentifier.DROPPING_OUT_REQUESTS);
        List<RequestDTO> droppingOutRequestDTOs = new ArrayList<>();
        droppingOutRequests.stream()
                .map(e -> (DroppingOutRequest) e)
                .filter(e -> e.getRequestingStudent().getDepartmentId().equals(departmentId))
                .forEach(e -> {
                    RequestDTO requestDTO = new RequestDTO();
                    requestDTO.setId(e.getId());
                    requestDTO.setRequestingStudentId(e.getRequestingStudent().getId());
                    requestDTO.setRequestingStudentName(e.getRequestingStudent().fetchName());
                    droppingOutRequestDTOs.add(requestDTO);
                });
        return droppingOutRequestDTOs;
    }

    public static void acceptDroppingOutRequest(DatabaseManager databaseManager, String requestingStudentId,
                                                String academicRequestId) {
        DroppingOutRequest droppingOutRequest = (DroppingOutRequest) databaseManager.get(
                DatasetIdentifier.DROPPING_OUT_REQUESTS, academicRequestId);
        droppingOutRequest.setRequestStatus(AcademicRequestStatus.ACCEPTED);
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, requestingStudentId);
        student.setStudentStatus(StudentStatus.DROPPED_OUT);
    }

    public static void removeDroppingOutRequest(DatabaseManager databaseManager, String academicRequestId) {
        databaseManager.remove(DatasetIdentifier.DROPPING_OUT_REQUESTS, academicRequestId);
    }
}
