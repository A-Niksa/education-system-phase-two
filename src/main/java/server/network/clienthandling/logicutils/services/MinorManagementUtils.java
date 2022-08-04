package server.network.clienthandling.logicutils.services;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.abstractions.Department;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.academicrequests.MinorRequest;
import shareables.models.pojos.users.students.Student;
import shareables.network.DTOs.MinorRequestDTO;
import shareables.network.DTOs.RequestDTO;

import java.util.ArrayList;
import java.util.List;

public class MinorManagementUtils {
    public static List<RequestDTO> getProfessorMinorRequestDTOs(DatabaseManager databaseManager, String professorId) {
        List<Identifiable> minorRequests = databaseManager.getIdentifiables(DatasetIdentifier.MINOR_REQUESTS);
        List<RequestDTO> minorRequestDTOs = new ArrayList<>();
        minorRequests.stream()
                .map(e -> (MinorRequest) e)
                .filter(e -> professorIsOriginOrTargetDepartmentDeputy(databaseManager, e, professorId))
                .filter(e -> e.getRequestStatus() == AcademicRequestStatus.SUBMITTED)
                .forEach(e -> {
                    MinorRequestDTO minorRequestDTO = new MinorRequestDTO();
                    minorRequestDTO.setId(e.getId());
                    minorRequestDTO.setOriginDepartmentId(e.getOriginDepartmentId());
                    minorRequestDTO.setTargetDepartmentId(e.getTargetDepartmentId());
                    Student student = IdentifiableFetchingUtils.getStudent(databaseManager, e.getRequestingStudentId());
                    minorRequestDTO.setRequestingStudentId(student.getId());
                    minorRequestDTO.setRequestingStudentName(student.fetchName());
                    minorRequestDTO.setRequestingStudentGPAString(student.fetchGPAString());
                    minorRequestDTOs.add(minorRequestDTO);
                });
        return minorRequestDTOs;
    }

    private static boolean professorIsOriginOrTargetDepartmentDeputy(DatabaseManager databaseManager,
                                                                     MinorRequest minorRequest, String professorId) {
        return professorIsInDepartmentAsDeputy(databaseManager, minorRequest.getOriginDepartmentId(), professorId) ||
                professorIsInDepartmentAsDeputy(databaseManager, minorRequest.getTargetDepartmentId(), professorId);
    }

    private static boolean professorIsInDepartmentAsDeputy(DatabaseManager databaseManager, String departmentId,
                                                           String professorId) {
        Department department = (Department) databaseManager.get(DatasetIdentifier.DEPARTMENTS, departmentId);
        return department.getDeputyId().equals(professorId);
    }

    public static void acceptMinorRequest(DatabaseManager databaseManager, String academicRequestId, String acceptingDepartmentId) {
        MinorRequest minorRequest = (MinorRequest) databaseManager.get(DatasetIdentifier.MINOR_REQUESTS, academicRequestId);
        if (departmentIsTheOriginDepartment(minorRequest, acceptingDepartmentId)) {
            minorRequest.setRequestStatusAtOrigin(AcademicRequestStatus.ACCEPTED);
        } else {
            minorRequest.setRequestStatusAtTarget(AcademicRequestStatus.ACCEPTED);
        }
        updateMinorRequestStatus(minorRequest);
        updateStudentMinorDegreeIfNecessary(databaseManager, minorRequest);
    }

    public static void declineMinorRequest(DatabaseManager databaseManager, String academicRequestId, String decliningDepartmentId) {
        MinorRequest minorRequest = (MinorRequest) databaseManager.get(DatasetIdentifier.MINOR_REQUESTS, academicRequestId);
        if (departmentIsTheOriginDepartment(minorRequest, decliningDepartmentId)) {
            minorRequest.setRequestStatusAtOrigin(AcademicRequestStatus.DECLINED);
        } else {
            minorRequest.setRequestStatusAtTarget(AcademicRequestStatus.DECLINED);
        }
        updateMinorRequestStatus(minorRequest);
        updateStudentMinorDegreeIfNecessary(databaseManager, minorRequest);
    }

    private static void updateMinorRequestStatus(MinorRequest minorRequest) {
        minorRequest.setRequestStatus(MinorSubmissionUtils.determineMinorRequestStatus(minorRequest));
    }

    private static void updateStudentMinorDegreeIfNecessary(DatabaseManager databaseManager, MinorRequest minorRequest) {
        if (minorRequest.getRequestStatus() == AcademicRequestStatus.ACCEPTED) {
            Student requestingStudent = IdentifiableFetchingUtils.getStudent(databaseManager,
                    minorRequest.getRequestingStudentId());
            requestingStudent.setMinorDepartmentId(minorRequest.getTargetDepartmentId());
        }
    }

    public static boolean departmentIsTheOriginDepartment(MinorRequest minorRequest, String departmentId) {
        return minorRequest.getOriginDepartmentId().equals(departmentId);
    }
}
